package com.gempukku.lotro.tournament;

import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.Map;
import java.util.Set;

public interface TournamentPlayerDAO {
    public void addPlayer(String tournamentId, String playerName, LotroDeck deck);

    public void updatePlayerDeck(String tournamentId, String playerName, LotroDeck deck);

    public void dropPlayer(String tournamentId, String playerName);

    public Map<String, LotroDeck> getPlayerDecks(String tournamentId);

    public Set<String> getDroppedPlayers(String tournamentId);

    public LotroDeck getPlayerDeck(String tournamentId, String playerName);

    public Set<String> getPlayers(String tournamentId);
}
