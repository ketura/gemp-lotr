package com.gempukku.lotro.logic.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.SubCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PutCardFromPlayOnTopOfDeckEffect;
import com.gempukku.lotro.logic.timing.Action;

public class ChooseAndPutCardFromPlayOnTopOfDeckEffect extends ChooseActiveCardEffect {
    private Action _action;
    private CostToEffectAction _resultSubAction;

    public ChooseAndPutCardFromPlayOnTopOfDeckEffect(Action action, String playerId, Filterable... filters) {
        super(action.getActionSource(), playerId, "Choose a card to put on top of deck", filters);
        _action = action;
    }

    @Override
    protected void cardSelected(LotroGame game, PhysicalCard card) {
        _resultSubAction = new SubCostToEffectAction(_action);
        _resultSubAction.appendEffect(new PutCardFromPlayOnTopOfDeckEffect(card));
        game.getActionsEnvironment().addActionToStack(_resultSubAction);
    }

    @Override
    public boolean wasCarriedOut() {
        return super.wasCarriedOut() && _resultSubAction != null && _resultSubAction.wasCarriedOut();
    }
}
