package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ChooseAndDiscardStackedCardsEffect extends AbstractEffect {
    private Action _action;
    private String _playerId;
    private int _minimum;
    private int _maximum;
    private Filter _stackedOnFilter;
    private Filter _stackedCardFilter;

    public ChooseAndDiscardStackedCardsEffect(Action action, String playerId, int minimum, int maximum, Filter stackedOnFilter, Filter stackedCardFilter) {
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
        return "Discard stacked card";
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.countActive(game.getGameState(), game.getModifiersQuerying(), _stackedOnFilter, Filters.hasStacked(_stackedCardFilter)) > 0;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        List<PhysicalCard> discardableCards = new LinkedList<PhysicalCard>();

        for (PhysicalCard stackedOnCard : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _stackedOnFilter))
            discardableCards.addAll(Filters.filter(game.getGameState().getStackedCards(stackedOnCard), game.getGameState(), game.getModifiersQuerying(), _stackedCardFilter));

        final boolean success = discardableCards.size() >= _minimum;

        if (discardableCards.size() <= _minimum) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(new DiscardStackedCardsEffect(_action.getActionSource(), discardableCards));
            game.getActionsEnvironment().addActionToStack(subAction);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose card(s) to discard", discardableCards, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            SubAction subAction = new SubAction(_action);
                            subAction.appendEffect(new DiscardStackedCardsEffect(_action.getActionSource(), selectedCards));
                            game.getActionsEnvironment().addActionToStack(subAction);
                        }
                    });
        }

        return new FullEffectResult(null, success, success);
    }
}
