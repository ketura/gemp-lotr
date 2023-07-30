package com.gempukku.lotro.modifiers.evaluator;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class SingleMemoryEvaluator implements Evaluator {
    private Integer _rememberedValue;
    private final Evaluator _evaluator;

    public SingleMemoryEvaluator(Evaluator evaluator) {
        _evaluator = evaluator;
    }

    @Override
    public int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected) {
        if (_rememberedValue == null)
            _rememberedValue = _evaluator.evaluateExpression(game, cardAffected);
        return _rememberedValue;
    }
}

