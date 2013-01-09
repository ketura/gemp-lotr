package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.common.ApplicationConfiguration;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

public class WebRequestHandler implements UriRequestHandler {
    private String _root = ApplicationConfiguration.getProperty("web.folder");

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
            responseWriter.writeFile(file);
        }
    }
}
