package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class WoundResult extends EffectResult {
    private PhysicalCard _card;

    public WoundResult(PhysicalCard card) {
        super(EffectResult.Type.WOUND);
        _card = card;
    }

    public PhysicalCard getWoundedCard() {
        return _card;
    }
}
