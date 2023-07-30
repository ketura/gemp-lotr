package com.gempukku.lotro.modifiers.condition.lotronly;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.PhaseCondition;

public class FierceSkirmishCondition extends PhaseCondition {
    public FierceSkirmishCondition() {
        super(Phase.SKIRMISH);
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return super.isFullfilled(game) && game.getGameState().isFierceSkirmishes();
    }
}
