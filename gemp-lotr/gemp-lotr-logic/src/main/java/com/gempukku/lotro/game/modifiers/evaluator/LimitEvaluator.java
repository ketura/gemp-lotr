package com.gempukku.lotro.game.modifiers.evaluator;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class LimitEvaluator implements Evaluator {
    private final Evaluator _limit;
    private final Evaluator _value;

    public LimitEvaluator(Evaluator valueEvaluator, Evaluator limitEvaluator) {
        _value = valueEvaluator;
        _limit = limitEvaluator;
    }

    @Override
    public int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected) {
        return Math.min( _limit.evaluateExpression(game, cardAffected), _value.evaluateExpression(game, cardAffected));
    }
}
