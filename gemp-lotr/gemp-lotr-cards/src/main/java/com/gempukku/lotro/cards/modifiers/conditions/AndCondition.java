package com.gempukku.lotro.cards.modifiers.conditions;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class AndCondition implements Condition {
    private Condition[] _conditions;

    public AndCondition(Condition... conditions) {
        _conditions = conditions;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        for (Condition condition : _conditions) {
            if (!condition.isFullfilled(gameState, modifiersQuerying))
                return false;
        }

        return true;
    }
}
