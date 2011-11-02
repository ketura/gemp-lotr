package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class NegativeEvaluator implements Evaluator {
    private Evaluator _source;

    public NegativeEvaluator(Evaluator source) {
        _source = source;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -_source.evaluateExpression(gameState, modifiersQuerying, self);
    }
}
