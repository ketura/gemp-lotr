package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.LinkedList;

public abstract class ChooseCardsFromDiscardEffect extends AbstractEffect {
    private String _playerId;
    private int _minimum;
    private int _maximum;
    private Filter _filter;

    public ChooseCardsFromDiscardEffect(String playerId, int minimum, int maximum, Filter... filters) {
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _filter = Filters.and(filters);
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        Collection<PhysicalCard> cards = Filters.filter(game.getGameState().getDiscard(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter);
        return cards.size() >= _minimum;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        Collection<PhysicalCard> cards = Filters.filter(game.getGameState().getDiscard(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter);

        boolean success = cards.size() >= _minimum;

        int minimum = Math.min(_minimum, cards.size());

        if (cards.size() == minimum)
            cardsSelected(game, cards);
        else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Choose card from discard", new LinkedList<PhysicalCard>(cards), minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            cardsSelected(game, getSelectedCardsByResponse(result));
                        }
                    });
        }

        return new FullEffectResult(null, success, success);
    }

    protected abstract void cardsSelected(LotroGame game, Collection<PhysicalCard> cards);
}
