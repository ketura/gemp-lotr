package com.gempukku.lotro.logic.timing;

public abstract class AbstractEffect implements Effect {
    private boolean _cancelled;
    private boolean _failed;

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
}
