package com.gempukku.lotro.effects;

import com.gempukku.lotro.actions.ActionProxy;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;

public class AddUntilStartOfPhaseActionProxyEffect extends UnrespondableEffect {
    private final ActionProxy _actionProxy;
    private final Phase _phase;

    public AddUntilStartOfPhaseActionProxyEffect(ActionProxy actionProxy, Phase phase) {
        _actionProxy = actionProxy;
        _phase = phase;
    }

    @Override
    public void doPlayEffect(DefaultGame game) {
        game.getActionsEnvironment().addUntilStartOfPhaseActionProxy(_actionProxy, _phase);
    }
}
