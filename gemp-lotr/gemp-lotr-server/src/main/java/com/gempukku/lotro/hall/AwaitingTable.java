package com.gempukku.lotro.hall;

import com.gempukku.lotro.game.LotroGameParticipant;

import java.util.*;

public class AwaitingTable {
    private Map<String, LotroGameParticipant> _players = new HashMap<String, LotroGameParticipant>();

    private int _capacity = 2;

    public boolean addPlayer(LotroGameParticipant player) {
        _players.put(player.getPlayerId(), player);
        return _players.size() == _capacity;
    }

    public boolean removePlayer(String playerId) {
        _players.remove(playerId);
        return _players.size() == 0;
    }

    public boolean hasPlayer(String playerId) {
        return _players.containsKey(playerId);
    }

    public Set<String> getPlayerNames() {
        return Collections.unmodifiableSet(_players.keySet());
    }

    public Set<LotroGameParticipant> getPlayers() {
        return Collections.unmodifiableSet(new HashSet<LotroGameParticipant>(_players.values()));
    }
}
