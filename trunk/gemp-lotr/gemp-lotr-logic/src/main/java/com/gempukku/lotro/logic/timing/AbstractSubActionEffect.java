package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;

public abstract class AbstractSubActionEffect implements Effect {
    private SubAction _subAction;

    protected void processSubAction(LotroGame game, SubAction subAction) {
        _subAction = subAction;
        game.getActionsEnvironment().addActionToStack(_subAction);
    }

    @Override
    public boolean wasSuccessful() {
        return _subAction != null && _subAction.wasSuccessful();
    }

    @Override
    public boolean wasCarriedOut() {
        return _subAction != null && _subAction.wasCarriedOut();
    }
}
