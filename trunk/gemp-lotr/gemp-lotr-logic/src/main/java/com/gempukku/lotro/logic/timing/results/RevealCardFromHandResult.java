package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class RevealCardFromHandResult extends EffectResult {
    private PhysicalCard _source;
    private String _playerId;
    private PhysicalCard _card;

    public RevealCardFromHandResult(PhysicalCard source, String playerId, PhysicalCard card) {
        super(Type.REVEAL_CARDS_FROM_HAND);
        _source = source;
        _playerId = playerId;
        _card = card;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public PhysicalCard getRevealedCard() {
        return _card;
    }
}
