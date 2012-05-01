package com.gempukku.lotro.tournament;

import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.Map;

public interface TournamentCallback {
    public void createGames(Map<String, String> pairings, Map<String, LotroDeck> playerDecks);
}
