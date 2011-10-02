package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class DiscardCardFromHandResult extends EffectResult {
    private PhysicalCard _source;
    private PhysicalCard _card;

    public DiscardCardFromHandResult(PhysicalCard source, PhysicalCard card) {
        super(Type.DISCARD_FROM_HAND);
        _source = source;
        _card = card;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public PhysicalCard getDiscardedCard() {
        return _card;
    }
}