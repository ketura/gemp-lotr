package com.gempukku.lotro.cards.modifiers.conditions;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ThreatCondition implements Condition {
    private int _count;

    public ThreatCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return gameState.getThreats() >= _count;
    }
}
