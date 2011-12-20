package com.gempukku.lotro.hall;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSerie;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.game.LotroGameParticipant;

import java.util.*;

public class AwaitingTable {
    private String _formatName;
    private String _collectionType;
    private LotroFormat _lotroFormat;
    private League _league;
    private LeagueSerie _leagueSerie;
    private Map<String, LotroGameParticipant> _players = new HashMap<String, LotroGameParticipant>();

    private int _capacity = 2;

    public AwaitingTable(String formatName, String collectionType, LotroFormat lotroFormat, League league, LeagueSerie leagueSerie) {
        _formatName = formatName;
        _collectionType = collectionType;
        _lotroFormat = lotroFormat;
        _league = league;
        _leagueSerie = leagueSerie;
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

    public Set<String> getPlayerNames() {
        return Collections.unmodifiableSet(_players.keySet());
    }

    public Set<LotroGameParticipant> getPlayers() {
        return Collections.unmodifiableSet(new HashSet<LotroGameParticipant>(_players.values()));
    }

    public String getFormatName() {
        return _formatName;
    }

    public String getCollectionType() {
        return _collectionType;
    }

    public LotroFormat getLotroFormat() {
        return _lotroFormat;
    }

    public League getLeague() {
        return _league;
    }

    public LeagueSerie getLeagueSerie() {
        return _leagueSerie;
    }
}
