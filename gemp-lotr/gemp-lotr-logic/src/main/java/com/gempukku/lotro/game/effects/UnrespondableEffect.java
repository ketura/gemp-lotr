package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;

public abstract class UnrespondableEffect extends AbstractEffect {
    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    protected abstract void doPlayEffect(DefaultGame game);

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        doPlayEffect(game);
        return new FullEffectResult(true);
    }
}
