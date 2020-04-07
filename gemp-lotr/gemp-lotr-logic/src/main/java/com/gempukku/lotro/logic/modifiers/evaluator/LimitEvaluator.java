package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class LimitEvaluator implements Evaluator {
    private int _limit;
    private Evaluator _evaluator;

    public LimitEvaluator(Evaluator evaluator, int limit) {
        _evaluator = evaluator;
        _limit = limit;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
        return Math.min(_limit, _evaluator.evaluateExpression(game, cardAffected));
    }
}
