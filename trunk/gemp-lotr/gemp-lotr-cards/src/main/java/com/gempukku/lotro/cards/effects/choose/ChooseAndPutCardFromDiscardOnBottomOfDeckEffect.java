package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndPutCardFromDiscardOnBottomOfDeckEffect extends ChooseCardsFromDiscardEffect {
    private Action _action;

    public ChooseAndPutCardFromDiscardOnBottomOfDeckEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(playerId, minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
        if (cards.size() > 0) {
            SubAction subAction = new SubAction(_action);
            for (PhysicalCard card : cards)
                subAction.appendEffect(new PutCardFromDiscardOnBottomOfDeckEffect(card));
            game.getActionsEnvironment().addActionToStack(subAction);
        }
    }
}
