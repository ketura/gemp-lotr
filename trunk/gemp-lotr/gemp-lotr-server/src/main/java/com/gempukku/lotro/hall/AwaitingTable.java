package com.gempukku.lotro.hall;

import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.game.LotroGameParticipant;
import com.gempukku.lotro.league.LeagueSerieData;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AwaitingTable {
    private CollectionType _collectionType;
    private LotroFormat _lotroFormat;
    private League _league;
    private LeagueSerieData _leagueSerie;
    private Map<String, LotroGameParticipant> _players = new HashMap<String, LotroGameParticipant>();

    private int _capacity = 2;

    public AwaitingTable(LotroFormat lotroFormat, CollectionType collectionType, League league, LeagueSerieData leagueSerie) {
        _lotroFormat = lotroFormat;
        _collectionType = collectionType;
        _league = league;
        _leagueSerie = leagueSerie;
        _capacity = lotroFormat.getAdventure().isSolo() ? 1 : 2;
    }

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

    public List<String> getPlayerNames() {
        return new LinkedList<String>(_players.keySet());
    }

    public Set<LotroGameParticipant> getPlayers() {
        return Collections.unmodifiableSet(new HashSet<LotroGameParticipant>(_players.values()));
    }

    public CollectionType getCollectionType() {
        return _collectionType;
    }

    public LotroFormat getLotroFormat() {
        return _lotroFormat;
    }

    public League getLeague() {
        return _league;
    }

    public LeagueSerieData getLeagueSerie() {
        return _leagueSerie;
    }
}
