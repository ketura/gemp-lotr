package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.state.GameState;

public class SpotBurdensCondition implements Condition {
    private int _count;

    public SpotBurdensCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return gameState.getBurdens() >= _count;
    }
}
