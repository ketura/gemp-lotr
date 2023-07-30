package com.gempukku.lotro.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.decisions.ForEachYouSpotDecision;

public abstract class ForEachYouSpotEffect extends AbstractSuccessfulEffect {
    private final String _playerId;
    private final Filterable[] _filters;

    public ForEachYouSpotEffect(String playerId, Filterable... filters) {
        _playerId = playerId;
        _filters = filters;
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
