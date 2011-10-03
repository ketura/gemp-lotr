package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public abstract class AbstractNewEffect implements NewEffect {
    private Boolean _carriedOut;
    private Boolean _successful;

    protected abstract FullEffectResult playEffectReturningResult(LotroGame game);

    @Override
    public final EffectResult[] playEffect(LotroGame game) {
        FullEffectResult fullEffectResult = playEffectReturningResult(game);
        _carriedOut = fullEffectResult._carriedOut;
        _successful = fullEffectResult._successful;
        return fullEffectResult._results;
    }

    @Override
    public final boolean wasCarriedOut() {
        if (_carriedOut == null)
            throw new IllegalStateException("Effect has to be played first");
        return _carriedOut;
    }

    @Override
    public final boolean wasSuccessful() {
        if (_successful == null)
            throw new IllegalStateException("Effect has to be played first");
        return _successful;
    }

    @Override
    public void reset() {
        _carriedOut = null;
        _successful = null;
    }

    protected static class FullEffectResult {
        private EffectResult[] _results;
        private boolean _successful;
        private boolean _carriedOut;

        public FullEffectResult(EffectResult[] results, boolean successful, boolean carriedOut) {
            _results = results;
            _successful = successful;
            _carriedOut = carriedOut;
        }
    }
}
