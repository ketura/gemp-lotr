package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class LimitEvaluator implements Evaluator {
    private int _limit;
    private Evaluator _evaluator;

    public LimitEvaluator(Evaluator evaluator, int limit) {
        _evaluator = evaluator;
        _limit = limit;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
        return Math.min(_limit, _evaluator.evaluateExpression(gameState, modifiersQuerying, cardAffected));
    }
}
