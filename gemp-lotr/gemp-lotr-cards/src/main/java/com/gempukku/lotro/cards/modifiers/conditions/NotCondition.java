package com.gempukku.lotro.cards.modifiers.conditions;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class NotCondition implements Condition {
    private Condition _condition;

    public NotCondition(Condition condition) {
        _condition = condition;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return !_condition.isFullfilled(gameState, modifiersQuerying);
    }
}
