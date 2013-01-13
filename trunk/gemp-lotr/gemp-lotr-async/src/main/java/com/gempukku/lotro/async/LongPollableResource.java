package com.gempukku.lotro.async;

public interface LongPollableResource {
    public void registerForChanges(LongPollingResource longPollingResource);

    public void deregisterResource(LongPollingResource longPollingResource);
}
