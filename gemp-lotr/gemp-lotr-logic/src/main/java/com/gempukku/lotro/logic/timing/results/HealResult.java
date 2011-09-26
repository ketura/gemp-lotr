package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class HealResult extends EffectResult {
    private Collection<PhysicalCard> _cards;

    public HealResult(Collection<PhysicalCard> cards) {
        super(Type.HEAL);
        _cards = cards;
    }

    public Collection<PhysicalCard> getCards() {
        return _cards;
    }
}
