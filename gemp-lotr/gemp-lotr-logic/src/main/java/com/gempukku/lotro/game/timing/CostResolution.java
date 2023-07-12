package com.gempukku.lotro.game.timing;

import com.gempukku.lotro.game.effects.EffectResult;

public class CostResolution {
    private final EffectResult[] _effectResults;
    private final boolean _successful;

    public CostResolution(EffectResult[] effectResults, boolean successful) {
        _effectResults = effectResults;
        _successful = successful;
    }

    public EffectResult[] getEffectResults() {
        return _effectResults;
    }

    public boolean isSuccessful() {
        return _successful;
    }
}
