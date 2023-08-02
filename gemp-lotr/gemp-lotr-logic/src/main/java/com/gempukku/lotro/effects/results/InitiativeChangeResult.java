package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.effects.EffectResult;

public class InitiativeChangeResult extends EffectResult {
    private final Side _side;

    public InitiativeChangeResult(Side side) {
        super(Type.INITIATIVE_CHANGE);
        _side = side;
    }

    public Side getSide() {
        return _side;
    }
}
