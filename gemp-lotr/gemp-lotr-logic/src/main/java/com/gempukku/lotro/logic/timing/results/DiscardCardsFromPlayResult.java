package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class DiscardCardsFromPlayResult extends EffectResult {
    private PhysicalCard _source;
    private String _performingPlayer;
    private PhysicalCard _card;

    public DiscardCardsFromPlayResult(PhysicalCard source, String performingPlayer, PhysicalCard card) {
        super(EffectResult.Type.FOR_EACH_DISCARDED_FROM_PLAY);
        _source = source;
        _performingPlayer = performingPlayer;
        _card = card;
    }

    public String getPerformingPlayer() {
        return _performingPlayer;
    }

    public PhysicalCard getDiscardedCard() {
        return _card;
    }

    public PhysicalCard getSource() {
        return _source;
    }
}
