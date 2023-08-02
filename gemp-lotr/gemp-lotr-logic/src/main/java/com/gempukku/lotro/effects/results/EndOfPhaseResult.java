package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.effects.EffectResult;

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
