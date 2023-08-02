package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.effects.EffectResult;

public class TakeControlOfSiteResult extends EffectResult {
    private final String _playerId;

    public TakeControlOfSiteResult(String playerId) {
        super(Type.TAKE_CONTROL_OF_SITE);
        _playerId = playerId;
    }

    public String getPlayerId() {
        return _playerId;
    }
}
