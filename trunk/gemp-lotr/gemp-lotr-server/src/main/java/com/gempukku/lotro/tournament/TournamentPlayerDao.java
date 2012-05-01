package com.gempukku.lotro.tournament;

import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.Map;

public interface TournamentPlayerDao {
    public void addPlayer(String tournamentId, String playerName, LotroDeck deck);

    public void removePlayer(String tournamentId, String playerName);

    public Map<String, LotroDeck> getPlayers(String tournamentId);
}
