package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class ExertResult extends EffectResult {
    private Collection<PhysicalCard> _cards;

    public ExertResult(Collection<PhysicalCard> cards) {
        super(Type.EXERT);
        _cards = cards;
    }

    public Collection<PhysicalCard> getExertedCards() {
        return _cards;
    }
}
