package com.gempukku.lotro.async;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.lang.reflect.Type;
import java.util.Map;

public class RootUriRequestHandler implements UriRequestHandler {
    private String _serverContextPath ="/gemp-lotr-server/";
    private String _webContextPath="/gemp-lotr/";
    private HallRequestHandler _hallRequestHandler;
    private WebRequestHandler _webRequestHandler = new WebRequestHandler();
    private LoginRequestHandler _loginRequestHandler;
    private StatusRequestHandler _statusRequestHandler;
    private DeckRequestHandler _deckRequestHandler;

    public RootUriRequestHandler(Map<Type, Object> context) {
        _hallRequestHandler = new HallRequestHandler(context);
        _deckRequestHandler = new DeckRequestHandler(context);
        _loginRequestHandler = new LoginRequestHandler(context);
        _statusRequestHandler = new StatusRequestHandler(context);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) {
        if (uri.startsWith(_webContextPath)) {
            _webRequestHandler.handleRequest(uri.substring(_webContextPath.length()), request, context, responseWriter, e);
        } else if (uri.startsWith(_serverContextPath +"hall")) {
            _hallRequestHandler.handleRequest(uri.substring(_serverContextPath.length()+4), request, context, responseWriter, e);
        } else if (uri.startsWith(_serverContextPath+"deck")) {
            _deckRequestHandler.handleRequest(uri.substring(_serverContextPath.length()+4), request, context, responseWriter, e);
        } else if (uri.startsWith(_serverContextPath+"login")) {
            _loginRequestHandler.handleRequest(uri.substring(_serverContextPath.length()+5), request, context, responseWriter, e);
        } else if (uri.equals(_serverContextPath)) {
            _statusRequestHandler.handleRequest(uri.substring(_serverContextPath.length()), request, context, responseWriter, e);
        } else {
            responseWriter.writeError(404);
        }
    }
}
