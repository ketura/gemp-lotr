package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.ActionProxy;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class AddUntilStartOfPhaseActionProxyEffect extends UnrespondableEffect {
    private ActionProxy _actionProxy;
    private Phase _phase;

    public AddUntilStartOfPhaseActionProxyEffect(ActionProxy actionProxy, Phase phase) {
        _actionProxy = actionProxy;
        _phase = phase;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getActionsEnvironment().addUntilStartOfPhaseActionProxy(_actionProxy, _phase);
    }
}
