package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class ConstantEvaluator implements Evaluator {
    private final int _value;

    public ConstantEvaluator(int value) {
        _value = value;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard self) {
        return _value;
    }
}
