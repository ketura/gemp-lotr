package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class WoundResult extends EffectResult {
    private Collection<PhysicalCard> _cards;

    public WoundResult(Collection<PhysicalCard> cards) {
        super(EffectResult.Type.WOUND);
        _cards = cards;
    }

    public Collection<PhysicalCard> getWoundedCards() {
        return _cards;
    }
}
