package com.gempukku.lotro.logic.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubCostToEffectAction;
import com.gempukku.lotro.logic.effects.PutCardFromDiscardOnTopOfDeckEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndPutCardFromDiscardOnTopOfDeckEffect extends ChooseCardsFromDiscardEffect {
    private Action _action;

    public ChooseAndPutCardFromDiscardOnTopOfDeckEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(playerId, minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
        if (cards.size() > 0) {
            SubCostToEffectAction subAction = new SubCostToEffectAction(_action);
            for (PhysicalCard card : cards)
                subAction.appendEffect(new PutCardFromDiscardOnTopOfDeckEffect(card));
            game.getActionsEnvironment().addActionToStack(subAction);
        }
    }
}
