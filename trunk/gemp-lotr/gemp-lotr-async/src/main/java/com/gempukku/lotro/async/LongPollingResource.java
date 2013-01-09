package com.gempukku.lotro.async;

public interface LongPollingResource {
    public boolean isChanged();

    public void process();
}
