package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class StackActionEffect extends UnrespondableEffect {
    private Action _action;

    public StackActionEffect(Action action) {
        _action = action;
    }

    @Override
    protected void doPlayEffect(LotroGame game) {
        game.getActionsEnvironment().addActionToStack(_action);
    }
}
