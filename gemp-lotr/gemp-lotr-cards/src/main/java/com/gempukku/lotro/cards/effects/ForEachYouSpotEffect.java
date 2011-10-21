package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public abstract class ForEachYouSpotEffect extends AbstractSuccessfulEffect {
    private String _playerId;
    private Filterable[] _filters;

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
    public EffectResult[] playEffect(LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new ForEachYouSpotDecision(1, "Choose how many you wish to spot", game, Filters.and(_filters), Integer.MAX_VALUE) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        spottedCards(getValidatedResult(result));
                    }
                });
        return null;
    }

    protected abstract void spottedCards(int spotCount);
}
