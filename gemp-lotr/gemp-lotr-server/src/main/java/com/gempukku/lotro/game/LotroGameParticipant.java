package com.gempukku.lotro.game;

import com.gempukku.lotro.logic.vo.LotroDeck;

public class LotroGameParticipant {
    private String _playerId;
    private String _deckName;
    private LotroDeck _deck;

    public LotroGameParticipant(String playerId, String deckName, LotroDeck deck) {
        _playerId = playerId;
        _deckName = deckName;
        _deck = deck;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public LotroDeck getDeck() {
        return _deck;
    }

    public String getDeckName() {
        return _deckName;
    }
}
