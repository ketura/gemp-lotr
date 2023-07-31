package com.gempukku.lotro.gamestate;

import com.gempukku.lotro.decisions.AwaitingDecision;

public interface UserFeedback {
    void sendAwaitingDecision(String playerId, AwaitingDecision awaitingDecision);

    boolean hasPendingDecisions();
}
