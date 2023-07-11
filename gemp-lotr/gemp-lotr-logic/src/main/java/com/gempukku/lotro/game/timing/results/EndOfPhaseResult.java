package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.timing.EffectResult;

public class EndOfPhaseResult extends EffectResult {
    private final Phase _phase;

    public EndOfPhaseResult(Phase phase) {
        super(EffectResult.Type.END_OF_PHASE);
        _phase = phase;
    }

    public Phase getPhase() {
        return _phase;
    }
}
