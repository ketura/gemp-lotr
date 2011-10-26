package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.sun.istack.internal.NotNull;

import java.util.Collection;

public abstract class AbstractEffect implements Effect {
    private Boolean _carriedOut;
    private Boolean _successful;

    protected abstract
    @NotNull
    FullEffectResult playEffectReturningResult(LotroGame game);

    @Override
    public final Collection<? extends EffectResult> playEffect(LotroGame game) {
        FullEffectResult fullEffectResult = playEffectReturningResult(game);
        _carriedOut = fullEffectResult.isCarriedOut();
        _successful = fullEffectResult.isSuccessful();
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

    protected final String getAppendedTextNames(Collection<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(card.getBlueprint().getName() + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    protected final String getAppendedNames(Collection<PhysicalCard> cards) {
        StringBuilder sb = new StringBuilder();
        for (PhysicalCard card : cards)
            sb.append(GameUtils.getCardLink(card) + ", ");

        if (sb.length() == 0)
            return "none";
        else
            return sb.substring(0, sb.length() - 2);
    }

    protected static class FullEffectResult {
        private Collection<? extends EffectResult> _results;
        private boolean _successful;
        private boolean _carriedOut;

        public FullEffectResult(Collection<? extends EffectResult> results, boolean successful, boolean carriedOut) {
            _results = results;
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
