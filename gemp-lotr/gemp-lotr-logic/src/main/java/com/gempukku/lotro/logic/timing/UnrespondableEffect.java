package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public abstract class UnrespondableEffect implements Effect {
    private boolean _failed;
    private boolean _cancelled;

    @Override
    public String getText() {
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
    public EffectResult getRespondableResult() {
        return null;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return true;
    }

    @Override
    public abstract void playEffect(LotroGame game);
}
