package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.game.Player;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;

public class LoginRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    public LoginRequestHandler(Map<Type, Object> context) {
        super(context);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) throws Exception {
        if (uri.equals("") && request.getMethod() == HttpMethod.POST) {
            HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
            String login = getFormParameterSafely(postDecoder, "login");
            String password = getFormParameterSafely(postDecoder, "password");

            Player player = _playerDao.loginUser(login, password);
            if (player != null) {
                if (player.getType().contains("u")) {
                    responseWriter.writeResponse(null, logUserReturningHeaders(e, login));
                } else {
                    responseWriter.writeError(403);
                }
            } else {
                responseWriter.writeError(401);
            }

        } else {
            responseWriter.writeError(404);
        }
    }

    private Map<String, String> logUserReturningHeaders(MessageEvent e, String login) throws SQLException {
        _playerDao.updateLastLoginIp(login, e.getChannel().getRemoteAddress().toString());

        CookieEncoder cookieEncoder = new CookieEncoder(true);
        for (Map.Entry<String, String> cookie : _loggedUserHolder.logUser(login).entrySet())
            cookieEncoder.addCookie(cookie.getKey(), cookie.getValue());

        return Collections.singletonMap(SET_COOKIE, cookieEncoder.encode());
    }
}
