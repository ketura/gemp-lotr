package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;

import java.util.Collection;

public abstract class AbstractEffect implements Effect {
    private Boolean _carriedOut;
    protected boolean _prevented;

    protected abstract FullEffectResult playEffectReturningResult(DefaultGame game);

    @Override
    public final void playEffect(DefaultGame game) {
        FullEffectResult fullEffectResult = playEffectReturningResult(game);
        _carriedOut = fullEffectResult.isCarriedOut();
    }

    @Override
    public boolean wasCarriedOut() {
        if (_carriedOut == null)
            throw new IllegalStateException("Effect has to be played first");
        return _carriedOut && !_prevented;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    protected final String getAppendedTextNames(Collection<? extends LotroPhysicalCard> cards) {
        return GameUtils.getAppendedTextNames(cards);
    }

    protected final String getAppendedNames(Collection<? extends LotroPhysicalCard> cards) {
        return GameUtils.getAppendedNames(cards);
    }

    protected static class FullEffectResult {
        private final boolean _carriedOut;

        public FullEffectResult(boolean carriedOut) {
            _carriedOut = carriedOut;
        }

        public boolean isCarriedOut() {
            return _carriedOut;
        }
    }
}