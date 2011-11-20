package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.timing.EffectResult;

public class StartOfPhaseResult extends EffectResult {
    private Phase _phase;
    private String _playerId;

    public StartOfPhaseResult(Phase phase) {
        super(EffectResult.Type.START_OF_PHASE);
        _phase = phase;
    }

    public StartOfPhaseResult(Phase phase, String playerId) {
        super(EffectResult.Type.START_OF_PHASE);
        _phase = phase;
        _playerId = playerId;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public Phase getPhase() {
        return _phase;
    }
}
