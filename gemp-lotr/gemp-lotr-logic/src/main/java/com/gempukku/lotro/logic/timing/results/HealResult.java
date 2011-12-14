package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class HealResult extends EffectResult {
    private PhysicalCard _healedCard;

    public HealResult(PhysicalCard healedCard) {
        super(Type.FOR_EACH_HEALED);
        _healedCard = healedCard;
    }

    public PhysicalCard getHealedCard() {
        return _healedCard;
    }
}
