package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.timing.EffectResult;

public class ReturnCardsToHandResult extends EffectResult {
    private final PhysicalCard _card;

    public ReturnCardsToHandResult(PhysicalCard card) {
        super(Type.FOR_EACH_RETURNED_TO_HAND);
        _card = card;
    }

    public PhysicalCard getReturnedCard() {
        return _card;
    }
}
