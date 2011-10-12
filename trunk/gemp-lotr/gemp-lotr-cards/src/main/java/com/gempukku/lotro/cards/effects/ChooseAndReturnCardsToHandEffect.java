package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndReturnCardsToHandEffect extends ChooseActiveCardsEffect {
    private Action _action;

    public ChooseAndReturnCardsToHandEffect(Action action, String playerId, int minimum, int maximum, Filter... filters) {
        super(action.getActionSource(), playerId, "Choose cards to return to hand", minimum, maximum, filters);
        _action = action;
    }

    @Override
    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(new ReturnCardsToHandEffect(_action.getActionSource(), Filters.in(cards)));
        game.getActionsEnvironment().addActionToStack(subAction);
    }
}
