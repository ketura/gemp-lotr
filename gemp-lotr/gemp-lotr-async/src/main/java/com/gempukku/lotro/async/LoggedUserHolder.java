package com.gempukku.lotro.async;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.COOKIE;

public class LoggedUserHolder {
    private long _loggedUserExpireLength = 1000 * 60 * 10; // 10 minutes session length
    private long _expireCheckInterval = 1000 * 60; // check every minute

    private Map<String, String> _users = Collections.synchronizedMap(new HashMap<String, String>());
    private Map<String, Long> _lastAccess = Collections.synchronizedMap(new HashMap<String, Long>());
    private ReadWriteLock _readWriteLock = new ReentrantReadWriteLock();
    private ClearExpiredRunnable _clearExpiredRunnable;

    public void start() {
        _clearExpiredRunnable = new ClearExpiredRunnable();
        Thread thr = new Thread(_clearExpiredRunnable);
        thr.start();
    }

    public String getLoggedUser(HttpRequest request) {
        _readWriteLock.readLock().lock();
        try {
            CookieDecoder cookieDecoder = new CookieDecoder();
            Set<Cookie> cookies = cookieDecoder.decode(request.getHeader(COOKIE));
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("loggedUser")) {
                    String value = cookie.getValue();
                    if (value != null) {
                        String loggedUser = _users.get(value);
                        if (loggedUser != null) {
                            _lastAccess.put(value, System.currentTimeMillis());
                            return loggedUser;
                        }
                    }
                }
            }
            return null;
        } finally {
            _readWriteLock.readLock().unlock();
        }
    }

    public Map<String, String> logUser(String userName) {
        _readWriteLock.readLock().lock();
        try {
            Map<String, String> cookies = new HashMap<String, String>();
            String userValue = insertValueForUser(userName);
            cookies.put("loggedUser", userValue);
            _lastAccess.put(userValue, System.currentTimeMillis());
            return cookies;
        } finally {
            _readWriteLock.readLock().unlock();
        }
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

    private class ClearExpiredRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                _readWriteLock.writeLock().lock();
                try {
                    long currentTime =  System.currentTimeMillis();
                    Iterator<Map.Entry<String, Long>> iterator = _lastAccess.entrySet().iterator();
                    if (iterator.hasNext()) {
                        Map.Entry<String, Long> lastAccess = iterator.next();
                        long expireAt = lastAccess.getValue() + _loggedUserExpireLength;
                        if (expireAt < currentTime) {
                            String userValue = lastAccess.getKey();
                            _users.remove(userValue);
                            iterator.remove();
                        }
                    }
                } finally {
                    _readWriteLock.writeLock().unlock();
                }
                try {
                    Thread.sleep(_expireCheckInterval);
                } catch (InterruptedException exp) {

                }
            }
        }
    }
}
