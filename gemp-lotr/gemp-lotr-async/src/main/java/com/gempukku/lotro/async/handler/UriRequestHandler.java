package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.ResponseWriter;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.lang.reflect.Type;
import java.util.Map;

public interface UriRequestHandler {
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) throws Exception;
}
