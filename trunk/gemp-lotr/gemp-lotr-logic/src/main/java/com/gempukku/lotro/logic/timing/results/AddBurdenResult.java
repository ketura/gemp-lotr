package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class AddBurdenResult extends EffectResult {
    private PhysicalCard _source;

    public AddBurdenResult(PhysicalCard source) {
        super(EffectResult.Type.ADD_BURDEN);
        _source = source;
    }

    public PhysicalCard getSource() {
        return _source;
    }
}
