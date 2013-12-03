package com.gempukku.lotro.async;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
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

    private Map<String, String> _sessionIdsToUsers = new HashMap<String, String>();
    private Multimap<String, String> _usersToSessionIds = HashMultimap.create();

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
            String cookieHeader = request.getHeader(COOKIE);
            if (cookieHeader != null) {
                Set<Cookie> cookies = cookieDecoder.decode(cookieHeader);
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("loggedUser")) {
                        String value = cookie.getValue();
                        if (value != null) {
                            String loggedUser = _sessionIdsToUsers.get(value);
                            if (loggedUser != null) {
                                _lastAccess.put(value, System.currentTimeMillis());
                                return loggedUser;
                            }
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
        _readWriteLock.writeLock().lock();
        try {
            Map<String, String> cookies = new HashMap<String, String>();
            String userValue = insertValueForUser(userName);
            cookies.put("loggedUser", userValue);
            _lastAccess.put(userValue, System.currentTimeMillis());
            return cookies;
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public void forceLogoutUser(String userName) {
        _readWriteLock.writeLock().lock();
        try {
            final Collection<String> sessionIds = new HashSet<String>(_usersToSessionIds.get(userName));
            for (String sessionId : sessionIds) {
                _sessionIdsToUsers.remove(sessionId);
                _usersToSessionIds.remove(userName, sessionId);
            }
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    private char[] _chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    private String insertValueForUser(String userName) {
        Random rnd = new Random();
        String sessionId;
        do {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 20; i++)
                result.append(_chars[rnd.nextInt(_chars.length)]);
            sessionId = result.toString();
        } while (_sessionIdsToUsers.containsKey(sessionId));
        _sessionIdsToUsers.put(sessionId, userName);
        _usersToSessionIds.put(userName, sessionId);
        return sessionId;
    }

    private class ClearExpiredRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                _readWriteLock.writeLock().lock();
                try {
                    long currentTime = System.currentTimeMillis();
                    Iterator<Map.Entry<String, Long>> iterator = _lastAccess.entrySet().iterator();
                    if (iterator.hasNext()) {
                        Map.Entry<String, Long> lastAccess = iterator.next();
                        long expireAt = lastAccess.getValue() + _loggedUserExpireLength;
                        if (expireAt < currentTime) {
                            String sessionId = lastAccess.getKey();
                            final String userName = _sessionIdsToUsers.remove(sessionId);
                            if (userName != null)
                                _usersToSessionIds.remove(userName, sessionId);
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
