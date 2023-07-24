package com.gempukku.lotro.game.modifiers.evaluator;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class MultiplyEvaluator implements Evaluator {
    private final Evaluator _source;
    private final Evaluator _multiplier;

    public MultiplyEvaluator(Evaluator multiplier, Evaluator source) {
        _multiplier = multiplier;
        _source = source;
    }

    public MultiplyEvaluator(int multiplier, Evaluator source) {
        _multiplier = new ConstantEvaluator(multiplier);
        _source = source;
    }

    @Override
    public int evaluateExpression(DefaultGame game, LotroPhysicalCard self) {
        return _multiplier.evaluateExpression(game, self) * _source.evaluateExpression(game, self);
    }
}
