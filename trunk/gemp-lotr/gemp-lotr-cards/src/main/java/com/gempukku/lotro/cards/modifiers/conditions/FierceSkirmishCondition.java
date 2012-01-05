package com.gempukku.lotro.cards.modifiers.conditions;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class FierceSkirmishCondition extends PhaseCondition {
    public FierceSkirmishCondition() {
        super(Phase.SKIRMISH);
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return super.isFullfilled(gameState, modifiersQuerying) && gameState.isFierceSkirmishes();
    }
}
