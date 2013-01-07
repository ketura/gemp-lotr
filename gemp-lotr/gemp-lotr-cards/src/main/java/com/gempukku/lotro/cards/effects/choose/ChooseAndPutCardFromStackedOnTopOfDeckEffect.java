package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.effects.PutCardFromStackedOnTopOfDeckEffect;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndPutCardFromStackedOnTopOfDeckEffect extends ChooseStackedCardsEffect {
    private Action _action;

    public ChooseAndPutCardFromStackedOnTopOfDeckEffect(Action action, String playerId, int minimum, int maximum, Filterable stackedOn, Filterable... stackedCardsFilter) {
        super(action, playerId, minimum, maximum, stackedOn, Filters.and(stackedCardsFilter));
        _action = action;
    }

    @Override
    protected void cardsChosen(LotroGame game, Collection<PhysicalCard> stackedCards) {
        if (stackedCards.size() > 0) {
            SubAction subAction = new SubAction(_action);
            for (PhysicalCard card : stackedCards)
                subAction.appendEffect(new PutCardFromStackedOnTopOfDeckEffect(card));
            game.getActionsEnvironment().addActionToStack(subAction);
        }
    }
}
