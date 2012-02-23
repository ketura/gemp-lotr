package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.logic.timing.EffectResult;

public class TakeControlOfSiteResult extends EffectResult {
    private String _playerId;

    public TakeControlOfSiteResult(String playerId) {
        super(Type.TAKE_CONTROL_OF_SITE);
        _playerId = playerId;
    }

    public String getPlayerId() {
        return _playerId;
    }
}
