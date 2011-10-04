package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class AddBurdenResult extends EffectResult {
    private PhysicalCard _source;
    private int _count;

    public AddBurdenResult(PhysicalCard source, int count) {
        super(EffectResult.Type.ADD_BURDEN);
        _source = source;
        _count = count;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public int getCount() {
        return _count;
    }
}
