package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.ActionProxy;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddUntilEndOfPhaseActionProxyEffect extends UnrespondableEffect {
    private final ActionProxy _actionProxy;
    private final Phase _phase;

    public AddUntilEndOfPhaseActionProxyEffect(ActionProxy actionProxy) {
        this(actionProxy, null);
    }

    public AddUntilEndOfPhaseActionProxyEffect(ActionProxy actionProxy, Phase phase) {
        _actionProxy = actionProxy;
        _phase = phase;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        Phase phase = _phase;
        if (phase == null)
            phase = game.getGameState().getCurrentPhase();
        game.getActionsEnvironment().addUntilEndOfPhaseActionProxy(_actionProxy, phase);
    }
}
