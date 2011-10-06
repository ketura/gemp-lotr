package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.cards.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class StrengthModifier extends AbstractModifier {
    private Evaluator _evaluator;

    public StrengthModifier(PhysicalCard source, Filter affectFilter, int modifier) {
        this(source, affectFilter, null, modifier);
    }

    public StrengthModifier(PhysicalCard source, Filter affectFilter, Condition condition, int modifier) {
        this(source, affectFilter, condition, new ConstantEvaluator(modifier));
    }

    public StrengthModifier(PhysicalCard source, Filter affectFilter, Condition condition, Evaluator evaluator) {
        super(source, null, affectFilter, condition, new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER});
        _evaluator = evaluator;
    }

    @Override
    public String getText(GameState gameState, ModifiersQuerying modifiersQuerying) {
        final int value = _evaluator.evaluateExpression(gameState, modifiersQuerying);
        if (value >= 0)
            return "Strength +" + value;
        else
            return "Strength " + value;
    }

    @Override
    public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        return result + _evaluator.evaluateExpression(gameState, modifiersQuerying);
    }
}
