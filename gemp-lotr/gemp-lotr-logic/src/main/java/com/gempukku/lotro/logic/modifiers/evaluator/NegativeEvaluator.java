package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class NegativeEvaluator implements Evaluator {
    private Evaluator _source;

    public NegativeEvaluator(Evaluator source) {
        _source = source;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard self) {
        return -_source.evaluateExpression(game, self);
    }
}
