package com.gempukku.lotro.game;

import com.gempukku.lotro.logic.vo.LotroDeck;

public class LotroGameParticipant {
    private final String _playerId;
    private final LotroDeck _deck;

    public LotroGameParticipant(String playerId, LotroDeck deck) {
        _playerId = playerId;
        _deck = deck;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public LotroDeck getDeck() {
        return _deck;
    }
}
