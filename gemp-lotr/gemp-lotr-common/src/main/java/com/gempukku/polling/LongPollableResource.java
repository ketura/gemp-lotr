package com.gempukku.polling;

public interface LongPollableResource {
    public void registerForChanges(LongPollingResource longPollingResource);

    public void deregisterResource(LongPollingResource longPollingResource);
}
