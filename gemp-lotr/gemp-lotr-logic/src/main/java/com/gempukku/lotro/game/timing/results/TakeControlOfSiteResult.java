package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.game.effects.EffectResult;

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
