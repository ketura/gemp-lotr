package com.gempukku.lotro.game;

import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultUserFeedback implements UserFeedback {
    private Map<String, AwaitingDecision> _awaitingDecisionMap = new HashMap<String, AwaitingDecision>();
    private Map<String, String> _warnings = new HashMap<String, String>();

    private LotroGame _game;

    public DefaultUserFeedback(LotroGame game) {
        _game = game;
    }

    public void participantDecided(String playerId) {
        _awaitingDecisionMap.remove(playerId);
        _game.getGameState().playerDecisionFinished(playerId);
    }

    public AwaitingDecision getAwaitingDecision(String playerId) {
        return _awaitingDecisionMap.get(playerId);
    }

    @Override
    public void sendAwaitingDecision(String playerId, AwaitingDecision awaitingDecision) {
        _awaitingDecisionMap.put(playerId, awaitingDecision);
        _game.getGameState().playerDecisionStarted(playerId, awaitingDecision);
    }

    @Override
    public void sendWarning(String playerId, String warning) {
        _warnings.put(playerId, warning);
    }

    @Override
    public boolean hasPendingDecisions() {
        return _awaitingDecisionMap.size() > 0;
    }

    public String consumeWarning(String playerId) {
        return _warnings.remove(playerId);
    }

    public Set<String> getUsersPendingDecision() {
        return _awaitingDecisionMap.keySet();
    }
}
