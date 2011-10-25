package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.logic.timing.EffectResult;

public class InitiativeChangeResult extends EffectResult {
    private Side _side;

    public InitiativeChangeResult(Side side) {
        super(Type.INITIATIVE_CHANGE);
        _side = side;
    }

    public Side getSide() {
        return _side;
    }
}
