package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

import java.util.Collection;

public abstract class AbstractEffect implements Effect {
    private Boolean _carriedOut;
    private Boolean _successful;
    protected boolean _prevented;

    protected abstract FullEffectResult playEffectReturningResult(LotroGame game);

    @Override
    public final void playEffect(LotroGame game) {
        FullEffectResult fullEffectResult = playEffectReturningResult(game);
        _carriedOut = fullEffectResult.isCarriedOut();
        _successful = fullEffectResult.isSuccessful();
    }

    @Override
    public boolean wasCarriedOut() {
        if (_carriedOut == null)
            throw new IllegalStateException("Effect has to be played first");
        return _carriedOut && !_prevented;
    }

    protected final String getAppendedTextNames(Collection<PhysicalCard> cards) {
        return GameUtils.getAppendedTextNames(cards);
    }

    protected final String getAppendedNames(Collection<PhysicalCard> cards) {
        return GameUtils.getAppendedNames(cards);
    }

    protected static class FullEffectResult {
        private boolean _successful;
        private boolean _carriedOut;

        public FullEffectResult(boolean successful, boolean carriedOut) {
            _successful = successful;
            _carriedOut = carriedOut;
        }

        public boolean isSuccessful() {
            return _successful;
        }

        public boolean isCarriedOut() {
            return _carriedOut;
        }
    }
}