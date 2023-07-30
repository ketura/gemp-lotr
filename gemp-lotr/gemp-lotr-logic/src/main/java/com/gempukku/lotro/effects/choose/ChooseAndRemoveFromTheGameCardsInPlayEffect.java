package com.gempukku.lotro.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.effects.RemoveCardsFromTheGameEffect;
import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.actions.lotronly.SubAction;
import com.gempukku.lotro.actions.Action;

import java.util.Collection;

public class ChooseAndRemoveFromTheGameCardsInPlayEffect extends ChooseActiveCardsEffect {
    private final Action _action;
    private final String _playerId;
    private CostToEffectAction _resultSubAction;

    public ChooseAndRemoveFromTheGameCardsInPlayEffect(Action action, String playerId, int minimum, int maximum, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose cards to remove from play", minimum, maximum, filters);
        _action = action;
        _playerId = playerId;
    }

    @Override
    protected void cardsSelected(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        _resultSubAction = new SubAction(_action);
        _resultSubAction.appendEffect(new RemoveCardsFromTheGameEffect(_playerId, _action.getActionSource(), cards));
        game.getActionsEnvironment().addActionToStack(_resultSubAction);
    }

    @Override
    public boolean wasCarriedOut() {
        return super.wasCarriedOut() && _resultSubAction != null && _resultSubAction.wasCarriedOut();
    }
}
