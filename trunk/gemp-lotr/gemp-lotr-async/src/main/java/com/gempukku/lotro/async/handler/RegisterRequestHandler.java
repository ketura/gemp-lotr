package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.ResponseWriter;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.lang.reflect.Type;
import java.util.Map;

public class RegisterRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    public RegisterRequestHandler(Map<Type, Object> context) {
        super(context);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) throws Exception {
        if (uri.equals("") && request.getMethod() == HttpMethod.POST) {
            responseWriter.writeError(503);
//
//            HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
//            String login = getFormParameterSafely(postDecoder, "login");
//            String password = getFormParameterSafely(postDecoder, "password");
//            try {
//                if (_playerDao.registerUser(login, password, e.getRemoteAddress().toString())) {
//                    responseWriter.writeXmlResponse(null, logUserReturningHeaders(e, login));
//                } else {
//                    throw new HttpProcessingException(409);
//                }
//            } catch (LoginInvalidException exp) {
//                throw new HttpProcessingException(400);
//            }
//
        } else {
            responseWriter.writeError(404);
        }
    }
}
