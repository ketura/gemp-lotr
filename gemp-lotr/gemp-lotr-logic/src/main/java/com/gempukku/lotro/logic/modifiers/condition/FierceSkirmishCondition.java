package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;

public class FierceSkirmishCondition extends PhaseCondition {
    public FierceSkirmishCondition() {
        super(Phase.SKIRMISH);
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        return super.isFullfilled(game) && game.getGameState().isFierceSkirmishes();
    }
}
