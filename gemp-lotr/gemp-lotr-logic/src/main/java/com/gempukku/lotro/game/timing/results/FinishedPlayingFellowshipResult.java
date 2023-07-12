package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.game.effects.EffectResult;

public class FinishedPlayingFellowshipResult extends EffectResult {
    private final String _playerId;

    public FinishedPlayingFellowshipResult(String playerId) {
        super(Type.FINISHED_PLAYING_FELLOWSHIP);
        _playerId = playerId;
    }

    public String getPlayerId() {
        return _playerId;
    }
}
