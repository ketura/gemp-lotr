package com.gempukku.polling;

public interface LongPollableResource {
    /**
     * Registers the request for changes, however if there are any changes that can be consumed immediatelly, then
     * true is returned.
     * @param waitingRequest
     * @return
     */
    public boolean registerRequest(WaitingRequest waitingRequest);

    public void deregisterRequest(WaitingRequest waitingRequest);

    public void requestWaitingNotification();
}
