package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.timing.EffectResult;

public class StartOfPhaseResult extends EffectResult {
    private Phase _phase;

    public StartOfPhaseResult(Phase phase) {
        super(EffectResult.Type.START_OF_PHASE);
        _phase = phase;
    }

    public Phase getPhase() {
        return _phase;
    }
}
