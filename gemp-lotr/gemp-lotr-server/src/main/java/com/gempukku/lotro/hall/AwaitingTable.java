package com.gempukku.lotro.hall;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AwaitingTable {
    private Set<String> _players = new HashSet<String>();

    private int _capacity = 2;

    public boolean addPlayer(String playerId) {
        _players.add(playerId);
        return _players.size() == _capacity;
    }

    public boolean removePlayer(String playerId) {
        _players.remove(playerId);
        return _players.size() == 0;
    }

    public boolean hasPlayer(String playerId) {
        return _players.contains(playerId);
    }

    public Set<String> getPlayers() {
        return Collections.unmodifiableSet(_players);
    }
}
