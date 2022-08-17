package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class LimitEvaluator implements Evaluator {
    private final Evaluator _limit;
    private final Evaluator _value;

    public LimitEvaluator(Evaluator valueEvaluator, Evaluator limitEvaluator) {
        _value = valueEvaluator;
        _limit = limitEvaluator;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
        return Math.min( _limit.evaluateExpression(game, cardAffected), _value.evaluateExpression(game, cardAffected));
    }
}
