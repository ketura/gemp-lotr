package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class RemoveBurdenResult extends EffectResult {
    private String _performingPlayerId;
    private PhysicalCard _source;

    public RemoveBurdenResult(String performingPlayerId, PhysicalCard source) {
        super(EffectResult.Type.REMOVE_BURDEN);
        _performingPlayerId = performingPlayerId;
        _source = source;
    }

    public String getPerformingPlayerId() {
        return _performingPlayerId;
    }

    public PhysicalCard getSource() {
        return _source;
    }
}
