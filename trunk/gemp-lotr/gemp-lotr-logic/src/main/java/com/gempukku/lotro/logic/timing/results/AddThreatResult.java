package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class AddThreatResult extends EffectResult {
    private PhysicalCard _source;
    private int _count;

    public AddThreatResult(PhysicalCard source, int count) {
        super(Type.ADD_THREAT);
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
