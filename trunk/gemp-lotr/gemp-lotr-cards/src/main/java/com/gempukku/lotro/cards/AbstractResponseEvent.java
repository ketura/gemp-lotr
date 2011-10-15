package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;

public class AbstractResponseEvent extends AbstractResponseOldEvent {
    private int _twilightCost;

    public AbstractResponseEvent(Side side, int twilightCost, Culture culture, String name) {
        super(side, culture, name);
        _twilightCost = twilightCost;
    }

    @Override
    public int getTwilightCost() {
        return _twilightCost;
    }
}
