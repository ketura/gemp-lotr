package com.gempukku.lotro.tournament;

import com.gempukku.lotro.cards.lotronly.LotroDeck;

public interface TournamentCallback {
    void createGame(String playerOne, LotroDeck deckOne, String playerTwo, LotroDeck deckTwo);

    void broadcastMessage(String message);
}
