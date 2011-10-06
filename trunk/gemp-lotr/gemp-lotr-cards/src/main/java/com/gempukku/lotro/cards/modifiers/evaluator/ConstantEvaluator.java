package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ConstantEvaluator implements Evaluator {
    private int _value;

    public ConstantEvaluator(int value) {
        _value = value;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return _value;
    }
}
