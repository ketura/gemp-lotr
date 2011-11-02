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
    private SubAction _resultSubAction;

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
        _resultSubAction = new SubAction(_action);
        _resultSubAction.appendEffect(new DiscardCardsFromPlayEffect(_action.getActionSource(), Filters.in(cards)));
        game.getActionsEnvironment().addActionToStack(_resultSubAction);
    }

    protected void cardsToBeDiscardedCallback(Collection<PhysicalCard> cards) {

    }

    @Override
    public boolean wasSuccessful() {
        return _resultSubAction != null && _resultSubAction.wasSuccessful();
    }

    @Override
    public boolean wasCarriedOut() {
        return _resultSubAction != null && _resultSubAction.wasCarriedOut();
    }
}
