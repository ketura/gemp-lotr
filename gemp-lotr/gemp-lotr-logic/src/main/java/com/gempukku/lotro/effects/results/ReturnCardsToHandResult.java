package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

public class ReturnCardsToHandResult extends EffectResult {
    private final LotroPhysicalCard _card;

    public ReturnCardsToHandResult(LotroPhysicalCard card) {
        super(Type.FOR_EACH_RETURNED_TO_HAND);
        _card = card;
    }

    public LotroPhysicalCard getReturnedCard() {
        return _card;
    }
}
