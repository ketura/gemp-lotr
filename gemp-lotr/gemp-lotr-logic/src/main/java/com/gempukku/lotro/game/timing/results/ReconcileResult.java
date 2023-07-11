package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.game.timing.EffectResult;

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
