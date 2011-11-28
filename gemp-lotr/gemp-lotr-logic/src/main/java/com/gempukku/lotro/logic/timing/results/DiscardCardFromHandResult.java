package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class DiscardCardFromHandResult extends EffectResult {
    private PhysicalCard _source;
    private PhysicalCard _card;
    private boolean _forced;

    public DiscardCardFromHandResult(PhysicalCard source, PhysicalCard card, boolean forced) {
        super(Type.FOR_EACH_DISCARDED_FROM_HAND);
        _source = source;
        _card = card;
        _forced = forced;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public boolean isForced() {
        return _forced;
    }
}