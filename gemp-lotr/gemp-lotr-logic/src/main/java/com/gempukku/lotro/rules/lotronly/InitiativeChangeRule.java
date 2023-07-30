package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.effects.results.InitiativeChangeResult;

public class InitiativeChangeRule {
    private Side _initiativeSide = null;

    public void checkInitiativeChange(DefaultGame game) {
        if (game.getGameState().getCurrentPhase() != Phase.PUT_RING_BEARER) {
            Side initiativeSide = game.getModifiersQuerying().hasInitiative(game);
            if (initiativeSide != _initiativeSide) {
                _initiativeSide = initiativeSide;
                game.getActionsEnvironment().emitEffectResult(new InitiativeChangeResult(_initiativeSide));
            }
        }
    }
}
