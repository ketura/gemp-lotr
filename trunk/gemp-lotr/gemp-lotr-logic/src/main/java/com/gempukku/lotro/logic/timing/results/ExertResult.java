package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class ExertResult extends EffectResult {
    private PhysicalCard _card;

    public ExertResult(PhysicalCard card) {
        super(Type.FOR_EACH_EXERTED);
        _card = card;
    }

    public PhysicalCard getExertedCard() {
        return _card;
    }
}
