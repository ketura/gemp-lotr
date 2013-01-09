package com.gempukku.lotro.async;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

public class WebRequestHandler implements UriRequestHandler {
    private String _root = "I:\\dev\\projects\\gemp-lotr\\gemp-lotr-web\\src\\main\\webapp\\";

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) {
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
