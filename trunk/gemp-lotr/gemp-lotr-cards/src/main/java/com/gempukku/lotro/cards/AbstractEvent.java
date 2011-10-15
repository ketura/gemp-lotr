package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;

public abstract class AbstractEvent extends AbstractOldEvent {
    private int _twilightCost;

    public AbstractEvent(Side side, int twilightCost, Culture culture, String name, Phase... playableInPhases) {
        super(side, culture, name, playableInPhases);
        _twilightCost = twilightCost;
    }

    @Override
    public int getTwilightCost() {
        return _twilightCost;
    }
}
