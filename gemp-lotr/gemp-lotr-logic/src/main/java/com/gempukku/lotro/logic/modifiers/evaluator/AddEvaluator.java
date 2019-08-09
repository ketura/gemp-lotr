package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class AddEvaluator implements Evaluator {
    private Evaluator _source;
    private int _additional;

    public AddEvaluator(int additional, Evaluator source) {
        _additional = additional;
        _source = source;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
        return _additional + _source.evaluateExpression(game, cardAffected);
    }
}
