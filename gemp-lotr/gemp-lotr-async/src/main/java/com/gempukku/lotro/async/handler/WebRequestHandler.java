package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.IF_NONE_MATCH;

import org.jboss.netty.handler.codec.http.HttpRequest;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class WebRequestHandler implements UriRequestHandler {
    private String _root;

    public WebRequestHandler(String root) {
        _root = root;
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) throws Exception {
        if (uri.equals(""))
            uri = "index.html";

        uri = uri.replace('/', File.separatorChar);

        if ((uri.contains(".."))
                || uri.contains(File.separator + ".")
                || uri.startsWith(".") || uri.endsWith("."))
            throw new HttpProcessingException(403);

        File file = new File(_root + uri);
        if (!file.getCanonicalPath().startsWith(_root))
            throw new HttpProcessingException(403);

        if (!file.exists())
            throw new HttpProcessingException(404);

        final String etag = "\""+file.lastModified()+"\"";

        if (clientHasCurrentVersion(request, etag))
            throw new HttpProcessingException(304);

        responseWriter.writeFile(file, Collections.singletonMap(HttpHeaders.Names.ETAG, etag));
    }

    private boolean clientHasCurrentVersion(HttpRequest request, String etag) {
        String ifNoneMatch = request.getHeader(IF_NONE_MATCH);
        if (ifNoneMatch != null) {
            String[] clientKnownVersions = ifNoneMatch.split(",");
            for (String clientKnownVersion : clientKnownVersions) {
                if (clientKnownVersion.trim().equals(etag))
                    return true;
            }
        }
        return false;
    }
}
