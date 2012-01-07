package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.logic.timing.EffectResult;

public class ReconcileResult extends EffectResult {
    private String _playerId;

    public ReconcileResult(String playerId) {
        super(EffectResult.Type.RECONCILE);
        _playerId = playerId;
    }

    public String getPlayerId() {
        return _playerId;
    }
}
