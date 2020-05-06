package com.gempukku.polling;

public interface WaitingRequest {
    void processRequest();
    void forciblyRemoved();
    boolean isOneShot();
}
