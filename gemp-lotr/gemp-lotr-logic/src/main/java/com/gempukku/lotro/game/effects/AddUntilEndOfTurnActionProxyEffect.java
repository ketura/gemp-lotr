package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.actions.ActionProxy;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.UnrespondableEffect;

public class AddUntilEndOfTurnActionProxyEffect extends UnrespondableEffect {
    private final ActionProxy _actionProxy;

    public AddUntilEndOfTurnActionProxyEffect(ActionProxy actionProxy) {
        _actionProxy = actionProxy;
    }

    @Override
    public void doPlayEffect(DefaultGame game) {
        game.getActionsEnvironment().addUntilEndOfTurnActionProxy(_actionProxy);
    }
}