package com.gempukku.lotro;

import com.gempukku.lotro.logic.vo.LotroDeck;

public class LotroGameParticipant {
    private String _playerId;
    private LotroDeck _deck;

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
