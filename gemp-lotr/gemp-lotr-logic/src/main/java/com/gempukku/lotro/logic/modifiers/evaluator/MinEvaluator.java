package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class MinEvaluator implements Evaluator {
    private final int _limit;
    private final Evaluator _evaluator;

    public MinEvaluator(Evaluator evaluator, int limit) {
        _evaluator = evaluator;
        _limit = limit;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
        return Math.max(_limit, _evaluator.evaluateExpression(game, cardAffected));
    }
}
