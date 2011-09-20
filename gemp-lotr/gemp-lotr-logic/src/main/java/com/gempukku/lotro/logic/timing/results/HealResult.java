package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class HealResult extends EffectResult {
    private List<PhysicalCard> _cards;

    public HealResult(List<PhysicalCard> cards) {
        super(Type.HEAL);
        _cards = cards;
    }

    public List<PhysicalCard> getCards() {
        return _cards;
    }
}
