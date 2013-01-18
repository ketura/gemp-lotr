package com.gempukku.polling;

public interface LongPollingResource {
    public boolean isChanged();

    public void processIfNotProcessed();
}
