package com.gempukku.lotro.modifiers.evaluator;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class MinEvaluator implements Evaluator {
    private final int _limit;
    private final Evaluator _evaluator;

    public MinEvaluator(Evaluator evaluator, int limit) {
        _evaluator = evaluator;
        _limit = limit;
    }

    @Override
    public int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected) {
        return Math.max(_limit, _evaluator.evaluateExpression(game, cardAffected));
    }
}
