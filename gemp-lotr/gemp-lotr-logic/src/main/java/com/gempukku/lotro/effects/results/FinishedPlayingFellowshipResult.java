package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.effects.EffectResult;

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
