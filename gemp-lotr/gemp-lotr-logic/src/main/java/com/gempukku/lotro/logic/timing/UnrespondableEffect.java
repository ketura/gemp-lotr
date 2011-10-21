package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public abstract class UnrespondableEffect extends AbstractEffect {
    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    protected abstract void doPlayEffect(LotroGame game);

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        doPlayEffect(game);
        return new FullEffectResult(null, true, true);
    }
}
