package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class MoveLimitModifier extends AbstractModifier {
    private Evaluator _moveLimitModifier;

    public MoveLimitModifier(PhysicalCard source, int moveLimitModifier) {
        this(source, null, moveLimitModifier);
    }

    public MoveLimitModifier(PhysicalCard source, Condition condition, int moveLimitModifier) {
        this(source, condition, new ConstantEvaluator(moveLimitModifier));
    }

    public MoveLimitModifier(PhysicalCard source, Condition condition, Evaluator moveLimitModifier) {
        super(source, null, null, condition, ModifierEffect.MOVE_LIMIT_MODIFIER);
        _moveLimitModifier = moveLimitModifier;
    }

    @Override
    public int getMoveLimitModifier(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return _moveLimitModifier.evaluateExpression(gameState, modifiersQuerying, null);
    }
}
