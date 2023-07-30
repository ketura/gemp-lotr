package com.gempukku.lotro.effects;

import com.gempukku.lotro.decisions.AwaitingDecision;
import com.gempukku.lotro.game.DefaultGame;

public class PlayoutDecisionEffect extends AbstractSuccessfulEffect {
    private final String _playerId;
    private final AwaitingDecision _decision;

    public PlayoutDecisionEffect(String playerId, AwaitingDecision decision) {
        _playerId = playerId;
        _decision = decision;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public void playEffect(DefaultGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId, _decision);
    }
}
