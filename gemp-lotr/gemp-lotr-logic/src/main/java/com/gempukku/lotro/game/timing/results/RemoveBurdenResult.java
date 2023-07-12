package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class RemoveBurdenResult extends EffectResult {
    private final String _performingPlayerId;
    private final PhysicalCard _source;

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
