package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.decisions.AwaitingDecision;
import com.gempukku.lotro.game.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.game.timing.Effect;

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
