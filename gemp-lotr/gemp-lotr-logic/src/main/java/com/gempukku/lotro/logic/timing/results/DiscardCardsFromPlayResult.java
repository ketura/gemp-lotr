package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class DiscardCardsFromPlayResult extends EffectResult {
    private PhysicalCard _card;

    public DiscardCardsFromPlayResult(PhysicalCard card) {
        super(EffectResult.Type.FOR_EACH_DISCARDED_FROM_PLAY);
        _card = card;
    }

    public PhysicalCard getDiscardedCard() {
        return _card;
    }
}
