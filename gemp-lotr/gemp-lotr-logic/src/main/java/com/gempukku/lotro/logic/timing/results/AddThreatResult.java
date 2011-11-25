package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class AddThreatResult extends EffectResult {
    private PhysicalCard _source;

    public AddThreatResult(PhysicalCard source) {
        super(Type.ADD_THREAT);
        _source = source;
    }

    public PhysicalCard getSource() {
        return _source;
    }
}
