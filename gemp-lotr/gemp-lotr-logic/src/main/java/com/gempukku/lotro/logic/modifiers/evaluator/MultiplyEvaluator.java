package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class MultiplyEvaluator implements Evaluator {
    private final Evaluator _source;
    private final int _multiplier;

    public MultiplyEvaluator(int multiplier, Evaluator source) {
        _multiplier = multiplier;
        _source = source;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard self) {
        return _multiplier * _source.evaluateExpression(game, self);
    }
}
