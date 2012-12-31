package com.gempukku.lotro.tournament;

import com.gempukku.lotro.logic.vo.LotroDeck;

public interface TournamentCallback {
    public void createGame(String playerOne, LotroDeck deckOne, String playerTwo, LotroDeck deckTwo, boolean allowSpectators);

    public void broadcastMessage(String message);
}
