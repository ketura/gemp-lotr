package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class WoundResult extends EffectResult {
    private List<PhysicalCard> _cards;

    public WoundResult(List<PhysicalCard> cards) {
        super(EffectResult.Type.WOUND);
        _cards = cards;
    }

    public List<PhysicalCard> getWoundedCards() {
        return _cards;
    }
}
