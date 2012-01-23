package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class ReturnCardsToHandResult extends EffectResult {
    private PhysicalCard _card;

    public ReturnCardsToHandResult(PhysicalCard card) {
        super(Type.FOR_EACH_RETURNED_TO_HAND);
        _card = card;
    }

    public PhysicalCard getReturnedCard() {
        return _card;
    }
}
