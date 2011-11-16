package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;

public abstract class AbstractEvent extends AbstractOldEvent {
    private int _twilightCost;

    public AbstractEvent(Side side, int twilightCost, Culture culture, String name, Phase playableInPhase, Phase... additionalPlayableInPhases) {
        super(side, culture, name, playableInPhase, additionalPlayableInPhases);
        _twilightCost = twilightCost;
    }

    @Override
    public int getTwilightCost() {
        return _twilightCost;
    }
}
