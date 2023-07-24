package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

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
