package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.effects.RemoveCardsFromTheGameEffect;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;

public class ChooseAndRemoveFromTheGameCardsInPlayEffect extends ChooseActiveCardsEffect {
    private Action _action;
    private String _playerId;
    private SubAction _resultSubAction;

    public ChooseAndRemoveFromTheGameCardsInPlayEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose cards to remove from play", minimum, maximum, filters);
        _action = action;
        _playerId = playerId;
    }

    @Override
    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
        _resultSubAction = new SubAction(_action);
        _resultSubAction.appendEffect(new RemoveCardsFromTheGameEffect(_playerId, _action.getActionSource(), cards));
        game.getActionsEnvironment().addActionToStack(_resultSubAction);
    }

    @Override
    public boolean wasSuccessful() {
        return super.wasSuccessful() && _resultSubAction != null && _resultSubAction.wasSuccessful();
    }

    @Override
    public boolean wasCarriedOut() {
        return super.wasCarriedOut() && _resultSubAction != null && _resultSubAction.wasCarriedOut();
    }
}
