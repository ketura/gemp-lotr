package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.logic.timing.EffectResult;

public class RemoveBurdenResult extends EffectResult {
    private String _playerId;

    public RemoveBurdenResult(String playerId) {
        super(EffectResult.Type.REMOVE_BURDEN);
        _playerId = playerId;
    }

    public String getPlayerId() {
        return _playerId;
    }
}
