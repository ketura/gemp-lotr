package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class SingleMemoryEvaluator implements Evaluator {
    private Integer _rememberedValue;
    private final Evaluator _evaluator;

    public SingleMemoryEvaluator(Evaluator evaluator) {
        _evaluator = evaluator;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
        if (_rememberedValue == null)
            _rememberedValue = _evaluator.evaluateExpression(game, cardAffected);
        return _rememberedValue;
    }
}

