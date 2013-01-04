package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class DiscardCardsFromPlayResult extends EffectResult {
    private PhysicalCard _source;
    private PhysicalCard _card;

    public DiscardCardsFromPlayResult(PhysicalCard source, PhysicalCard card) {
        super(EffectResult.Type.FOR_EACH_DISCARDED_FROM_PLAY);
        _source = source;
        _card = card;
    }

    public PhysicalCard getDiscardedCard() {
        return _card;
    }

    public PhysicalCard getSource() {
        return _source;
    }
}
