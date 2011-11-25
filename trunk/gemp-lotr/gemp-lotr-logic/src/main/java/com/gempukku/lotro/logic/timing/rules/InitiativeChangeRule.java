package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.results.InitiativeChangeResult;

public class InitiativeChangeRule {
    private Side _initiativeSide = null;

    public void checkInitiativeChange(LotroGame game) {
        if (game.getGameState().getCurrentPhase() != Phase.PUT_RING_BEARER) {
            Side initiativeSide = game.getModifiersQuerying().hasInitiative(game.getGameState());
            if (initiativeSide != _initiativeSide) {
                _initiativeSide = initiativeSide;
                game.getActionsEnvironment().emitEffectResult(new InitiativeChangeResult(_initiativeSide));
            }
        }
    }
}
