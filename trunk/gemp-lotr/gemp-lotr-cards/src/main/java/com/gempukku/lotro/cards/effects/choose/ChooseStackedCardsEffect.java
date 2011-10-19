package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class ChooseStackedCardsEffect extends AbstractEffect {
    private Action _action;
    private String _playerId;
    private int _minimum;
    private int _maximum;
    private Filter _stackedOnFilter;
    private Filter _stackedCardFilter;

    public ChooseStackedCardsEffect(Action action, String playerId, int minimum, int maximum, Filter stackedOnFilter, Filter stackedCardFilter) {
        _action = action;
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _stackedOnFilter = stackedOnFilter;
        _stackedCardFilter = stackedCardFilter;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Choose stacked card";
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.countActive(game.getGameState(), game.getModifiersQuerying(), _stackedOnFilter, Filters.hasStacked(_stackedCardFilter)) > 0;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        List<PhysicalCard> stackedCards = new LinkedList<PhysicalCard>();

        for (PhysicalCard stackedOnCard : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _stackedOnFilter))
            stackedCards.addAll(Filters.filter(game.getGameState().getStackedCards(stackedOnCard), game.getGameState(), game.getModifiersQuerying(), _stackedCardFilter));

        final boolean success = stackedCards.size() >= _minimum;

        if (stackedCards.size() <= _minimum) {
            cardsChosen(stackedCards);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose cards", stackedCards, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> stackedCards = getSelectedCardsByResponse(result);
                            cardsChosen(stackedCards);
                        }
                    });
        }

        return new FullEffectResult(null, success, success);
    }

    protected abstract void cardsChosen(Collection<PhysicalCard> stackedCards);
}
