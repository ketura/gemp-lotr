package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.timing.EffectResult;

public class EndOfPhaseResult extends EffectResult {
    private Phase _phase;

    public EndOfPhaseResult(Phase phase) {
        super(EffectResult.Type.END_OF_PHASE);
        _phase = phase;
    }

    public Phase getPhase() {
        return _phase;
    }
}
