package com.gempukku.lotro.logic.timing;

public class CostResolution {
    private EffectResult[] _effectResults;
    private boolean _successful;

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
