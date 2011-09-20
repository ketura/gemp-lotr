package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class ExertResult extends EffectResult {
    private List<PhysicalCard> _cards;

    public ExertResult(List<PhysicalCard> cards) {
        super(Type.EXERT);
        _cards = cards;
    }

    public List<PhysicalCard> getExertedCards() {
        return _cards;
    }
}
