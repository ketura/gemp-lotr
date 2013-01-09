package com.gempukku.lotro.async;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.COOKIE;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.util.*;

public class LoggedUserHolder {
    private Map<String, String> _users = Collections.synchronizedMap(new HashMap<String, String>());

    public String getLoggedUser(HttpRequest request) {
        CookieDecoder cookieDecoder = new CookieDecoder();
        Set<Cookie> cookies = cookieDecoder.decode(request.getHeader(COOKIE));
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("loggedUser")) {
                String value = cookie.getValue();
                if (value != null) {
                    String loggedUser = _users.get(value);
                    if (loggedUser != null)
                        return loggedUser;
                }
            }
        }
        return null;
    }

    public Map<String, String> logUser(String userName) {
        Map<String, String> cookies = new HashMap<String, String>();
        cookies.put("loggedUser", insertValueForUser(userName));
        return cookies;
    }

    private char[] _chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    private synchronized String insertValueForUser(String userName) {
        Random rnd = new Random();
        String randomStr;
        do {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 20; i++)
                result.append(_chars[rnd.nextInt(_chars.length)]);
            randomStr = result.toString();
        } while (_users.containsKey(randomStr));
        _users.put(randomStr, userName);
        return randomStr;
    }
}
