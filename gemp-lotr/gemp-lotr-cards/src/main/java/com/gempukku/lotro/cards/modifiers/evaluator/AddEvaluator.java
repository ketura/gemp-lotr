package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class AddEvaluator implements Evaluator {
    private Evaluator _source;
    private int _additional;

    public AddEvaluator(int additional, Evaluator source) {
        _additional = additional;
        _source = source;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
        return _additional + _source.evaluateExpression(gameState, modifiersQuerying, cardAffected);
    }
}
