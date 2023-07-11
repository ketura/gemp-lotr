package com.gempukku.lotro.game.modifiers.evaluator;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.Condition;

public class ConditionEvaluator implements Evaluator {
    private final int _default;
    private final int _conditionFullfilled;
    private final Condition _condition;

    public ConditionEvaluator(int aDefault, int conditionFullfilled, Condition condition) {
        _default = aDefault;
        _conditionFullfilled = conditionFullfilled;
        _condition = condition;
    }

    @Override
    public int evaluateExpression(DefaultGame game, PhysicalCard self) {
        if (_condition.isFullfilled(game))
            return _conditionFullfilled;
        return _default;
    }
}
