package com.gempukku.lotro.db;

import com.gempukku.lotro.cache.Cached;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CachedIgnoreDAO implements IgnoreDAO, Cached {
    private final Map<String, Set<String>> ignores = new ConcurrentHashMap<>();
    private final IgnoreDAO delegate;

    public CachedIgnoreDAO(IgnoreDAO delegate) {
        this.delegate = delegate;
    }

    @Override
    public void clearCache() {
        ignores.clear();
    }

    @Override
    public int getItemCount() {
        return ignores.size();
    }

    @Override
    public Set<String> getIgnoredUsers(String playerId) {
        Set<String> ignoredUsers = ignores.get(playerId);
        if (ignoredUsers == null) {
            ignoredUsers = Collections.synchronizedSet(delegate.getIgnoredUsers(playerId));
            ignores.put(playerId, ignoredUsers);
        }
        return ignoredUsers;
    }

    @Override
    public boolean addIgnoredUser(String playerId, String ignoredName) {
        final Set<String> ignoredUsers = getIgnoredUsers(playerId);
        if (!ignoredUsers.contains(ignoredName)) {
            delegate.addIgnoredUser(playerId, ignoredName);
            ignoredUsers.add(ignoredName);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeIgnoredUser(String playerId, String ignoredName) {
        final Set<String> ignoredUsers = getIgnoredUsers(playerId);
        if (ignoredUsers.contains(ignoredName)) {
            delegate.removeIgnoredUser(playerId, ignoredName);
            ignoredUsers.remove(ignoredName);
            return true;
        }
        return false;
    }
}
