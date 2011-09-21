package com.gempukku.lotro.communication;

import com.gempukku.lotro.logic.decisions.AwaitingDecision;

public interface UserFeedback {
    public void sendAwaitingDecision(String playerId, AwaitingDecision awaitingDecision);

    public void sendWarning(String playerId, String warning);

    public boolean hasPendingDecisions();
}
