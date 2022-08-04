package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;

public abstract class ForEachYouSpotEffect extends AbstractSuccessfulEffect {
    private final String _playerId;
    private final Filterable[] _filters;

    public ForEachYouSpotEffect(String playerId, Filterable... filters) {
        _playerId = playerId;
        _filters = filters;
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
    public void playEffect(LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new ForEachYouSpotDecision(1, "Choose how many you wish to spot", game, _filters) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        spottedCards(getValidatedResult(result));
                    }
                });
    }

    protected abstract void spottedCards(int spotCount);
}
