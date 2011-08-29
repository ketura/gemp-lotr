package com.gempukku.lotro;

import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;

import java.util.HashMap;
import java.util.Map;

public class DefaultUserFeedback implements UserFeedback {
    private Map<String, AwaitingDecision> _awaitingDecisionMap = new HashMap<String, AwaitingDecision>();
    private Map<String, String> _warnings = new HashMap<String, String>();

    public void participantDecided(String playerId) {
        _awaitingDecisionMap.remove(playerId);
    }

    public AwaitingDecision getAwaitingDecision(String playerId) {
        return _awaitingDecisionMap.get(playerId);
    }

    public String consumeWarning(String playerId) {
        return _warnings.remove(playerId);
    }

    @Override
    public void sendAwaitingDecision(String playerId, AwaitingDecision awaitingDecision) {
        _awaitingDecisionMap.put(playerId, awaitingDecision);
    }

    @Override
    public boolean hasPendingDecisions() {
        return _awaitingDecisionMap.size() > 0;
    }

    @Override
    public void sendWarning(String playerId, String warning) {
        _warnings.put(playerId, warning);
    }
}
