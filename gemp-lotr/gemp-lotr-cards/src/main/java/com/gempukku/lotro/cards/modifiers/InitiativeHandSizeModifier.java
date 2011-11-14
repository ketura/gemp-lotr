package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class InitiativeHandSizeModifier extends AbstractModifier {
    private Condition _condition;
    private Evaluator _evaluator;

    public InitiativeHandSizeModifier(PhysicalCard source, int modifier) {
        this(source, null, modifier);
    }

    public InitiativeHandSizeModifier(PhysicalCard source, Condition condition, int modifier) {
        this(source, condition, new ConstantEvaluator(modifier));
    }

    public InitiativeHandSizeModifier(PhysicalCard source, Condition condition, Evaluator evaluator) {
        super(source, null, null, ModifierEffect.INITIATIVE_MODIFIER);
        _condition = condition;
        _evaluator = evaluator;
    }

    @Override
    public int getInitiativeHandSizeModifier(GameState gameState, ModifiersQuerying modifiersQuerying) {
        if (_condition == null || _condition.isFullfilled(gameState, modifiersQuerying))
            return _evaluator.evaluateExpression(gameState, modifiersQuerying, null);
        else
            return 0;
    }
}
