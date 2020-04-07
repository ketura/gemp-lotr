package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;

import java.util.Collection;

public abstract class AbstractSubActionEffect implements Effect {
    private CostToEffectAction _subAction;

    protected void processSubAction(LotroGame game, CostToEffectAction subAction) {
        _subAction = subAction;
        game.getActionsEnvironment().addActionToStack(_subAction);
    }

    protected final String getAppendedNames(Collection<? extends PhysicalCard> cards) {
        return GameUtils.getAppendedNames(cards);
    }

    @Override
    public boolean wasCarriedOut() {
        return _subAction != null && _subAction.wasCarriedOut();
    }
}
