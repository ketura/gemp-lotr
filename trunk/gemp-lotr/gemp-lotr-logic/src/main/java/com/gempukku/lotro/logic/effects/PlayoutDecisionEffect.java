package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class PlayoutDecisionEffect extends AbstractSuccessfulEffect {
    private String _playerId;
    private AwaitingDecision _decision;

    public PlayoutDecisionEffect(String playerId, AwaitingDecision decision) {
        _playerId = playerId;
        _decision = decision;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public Collection<? extends EffectResult> playEffect(LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId, _decision);
        return null;
    }
}
