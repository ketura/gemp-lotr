package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.timing.EffectResult;

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
