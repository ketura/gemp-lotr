package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.ResponseWriter;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.io.File;
import java.lang.reflect.Type;
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
                || uri.startsWith(".") || uri.endsWith(".")) {
            responseWriter.writeError(403);
        } else {
            File file = new File(_root + uri);
            if (!file.getCanonicalPath().startsWith(_root))
                responseWriter.writeError(403);
            else
                responseWriter.writeFile(file);
        }
    }
}
