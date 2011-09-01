package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class HealResult extends EffectResult {
    private PhysicalCard _card;

    public HealResult(PhysicalCard card) {
        super(Type.HEAL);
        _card = card;
    }

    public PhysicalCard getCard() {
        return _card;
    }
}
