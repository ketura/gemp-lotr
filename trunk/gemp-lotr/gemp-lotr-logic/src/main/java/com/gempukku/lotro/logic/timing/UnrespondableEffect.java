package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public abstract class UnrespondableEffect implements Effect {
    private boolean _failed;
    private boolean _cancelled;

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public void setFailed() {
        _failed = true;
    }

    @Override
    public boolean isFailed() {
        return _failed;
    }

    @Override
    public void cancel() {
        _cancelled = true;
    }

    @Override
    public boolean isCancelled() {
        return _cancelled;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return true;
    }

    protected abstract void doPlayEffect(LotroGame game);

    @Override
    public final EffectResult[] playEffect(LotroGame game) {
        doPlayEffect(game);
        return null;
    }
}
