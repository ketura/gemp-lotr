package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class ConditionEvaluator implements Evaluator {
    private int _default;
    private int _conditionFullfilled;
    private Condition _condition;

    public ConditionEvaluator(int aDefault, int conditionFullfilled, Condition condition) {
        _default = aDefault;
        _conditionFullfilled = conditionFullfilled;
        _condition = condition;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (_condition.isFullfilled(gameState, modifiersQuerying))
            return _conditionFullfilled;
        return _default;
    }
}
