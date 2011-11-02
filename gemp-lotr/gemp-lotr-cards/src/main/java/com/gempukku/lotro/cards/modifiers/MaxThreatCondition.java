package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class MaxThreatCondition implements Condition {
    private int _count;

    public MaxThreatCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return gameState.getThreats() <= _count;
    }
}
