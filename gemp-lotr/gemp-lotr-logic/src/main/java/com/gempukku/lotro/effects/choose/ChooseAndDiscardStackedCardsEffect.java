package com.gempukku.lotro.effects.choose;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.decisions.CardsSelectionDecision;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.SubAction;
import com.gempukku.lotro.effects.DiscardStackedCardsEffect;
import com.gempukku.lotro.effects.AbstractSubActionEffect;
import com.gempukku.lotro.actions.Action;
import com.gempukku.lotro.effects.Effect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ChooseAndDiscardStackedCardsEffect extends AbstractSubActionEffect {
    private final Action _action;
    private final String _playerId;
    private final int _minimum;
    private final int _maximum;
    private final Filterable _stackedOnFilter;
    private final Filterable _stackedCardFilter;

    public ChooseAndDiscardStackedCardsEffect(Action action, String playerId, int minimum, int maximum, Filterable stackedOnFilter, Filterable stackedCardFilter) {
        _action = action;
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _stackedOnFilter = stackedOnFilter;
        _stackedCardFilter = stackedCardFilter;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Discard stacked card";
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.countActive(game, _stackedOnFilter, Filters.hasStacked(_stackedCardFilter)) > 0;
    }

    @Override
    public void playEffect(final DefaultGame game) {
        List<LotroPhysicalCard> discardableCards = new LinkedList<>();

        for (LotroPhysicalCard stackedOnCard : Filters.filterActive(game, _stackedOnFilter))
            discardableCards.addAll(Filters.filter(game.getGameState().getStackedCards(stackedOnCard), game, _stackedCardFilter));

        if (discardableCards.size() <= _minimum) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(new DiscardStackedCardsEffect(_action.getActionSource(), discardableCards));
            discardingCardsCallback(discardableCards);
            processSubAction(game, subAction);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose cards to discard", discardableCards, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<LotroPhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            SubAction subAction = new SubAction(_action);
                            subAction.appendEffect(new DiscardStackedCardsEffect(_action.getActionSource(), selectedCards));
                            discardingCardsCallback(selectedCards);
                            processSubAction(game, subAction);
                        }
                    });
        }
    }

    protected void discardingCardsCallback(Collection<LotroPhysicalCard> cards) {

    }
}
