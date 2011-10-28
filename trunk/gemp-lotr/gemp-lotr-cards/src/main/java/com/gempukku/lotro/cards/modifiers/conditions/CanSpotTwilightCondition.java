package com.gempukku.lotro.cards.modifiers.conditions;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CanSpotTwilightCondition implements Condition {
    private int _count;

    public CanSpotTwilightCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return gameState.getTwilightPool() >= _count;
    }
}
