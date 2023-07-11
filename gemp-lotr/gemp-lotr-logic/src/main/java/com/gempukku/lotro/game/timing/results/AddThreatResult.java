package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.timing.EffectResult;

public class AddThreatResult extends EffectResult {
    private final PhysicalCard _source;

    public AddThreatResult(PhysicalCard source) {
        super(Type.ADD_THREAT);
        _source = source;
    }

    public PhysicalCard getSource() {
        return _source;
    }
}
