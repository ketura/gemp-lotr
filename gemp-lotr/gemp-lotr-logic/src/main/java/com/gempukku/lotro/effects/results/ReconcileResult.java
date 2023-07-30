package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.effects.EffectResult;

public class ReconcileResult extends EffectResult {
    private final String _playerId;

    public ReconcileResult(String playerId) {
        super(EffectResult.Type.RECONCILE);
        _playerId = playerId;
    }

    public String getPlayerId() {
        return _playerId;
    }
}
