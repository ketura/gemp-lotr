package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndDiscardCardsFromPlayEffect extends ChooseActiveCardsEffect {
    private Action _action;

    public ChooseAndDiscardCardsFromPlayEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose cards to discard", minimum, maximum, filters);
        _action = action;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
        cardsToBeDiscardedCallback(cards);
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(new DiscardCardsFromPlayEffect(_action.getActionSource(), Filters.in(cards)));
        game.getActionsEnvironment().addActionToStack(subAction);
    }

    protected void cardsToBeDiscardedCallback(Collection<PhysicalCard> cards) {

    }
}
