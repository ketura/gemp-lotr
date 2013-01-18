package com.gempukku.polling;

public interface LongPollingResource {
    public boolean isChanged();

    public boolean wasProcessed();

    public void processIfNotProcessed();
}
