package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class AddTwilightResult extends EffectResult {
    private PhysicalCard _source;

    public AddTwilightResult(PhysicalCard source) {
        super(EffectResult.Type.ADD_TWILIGHT);
        _source = source;
    }

    public PhysicalCard getSource() {
        return _source;
    }
}
