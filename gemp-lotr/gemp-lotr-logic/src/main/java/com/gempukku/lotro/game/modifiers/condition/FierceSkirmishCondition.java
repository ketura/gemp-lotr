package com.gempukku.lotro.game.modifiers.condition;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;

public class FierceSkirmishCondition extends PhaseCondition {
    public FierceSkirmishCondition() {
        super(Phase.SKIRMISH);
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return super.isFullfilled(game) && game.getGameState().isFierceSkirmishes();
    }
}
