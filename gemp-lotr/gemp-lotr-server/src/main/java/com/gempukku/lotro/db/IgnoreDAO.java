package com.gempukku.lotro.db;

import java.util.Set;

public interface IgnoreDAO {
    Set<String> getIgnoredUsers(String playerId);

    boolean addIgnoredUser(String playerId, String ignoredName);

    boolean removeIgnoredUser(String playerId, String ignoredName);
}
