package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class SanctuaryHealModifier extends AbstractModifier {
    private Evaluator evaluator;

    public SanctuaryHealModifier(PhysicalCard source, Condition condition, Evaluator amount) {
        super(source, "Sanctuary heal modifier "+amount, null, condition, ModifierEffect.SANCTUARY_HEAL_MODIFIER);
        evaluator = amount;
    }

    public SanctuaryHealModifier(PhysicalCard source, Condition condition, int amount) {
        super(source, "Sanctuary heal modifier "+amount, null, condition, ModifierEffect.SANCTUARY_HEAL_MODIFIER);
        evaluator = new ConstantEvaluator(amount);
    }

    @Override
    public int getSanctuaryHealModifier(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return evaluator.evaluateExpression(gameState, modifiersQuerying, null);
    }
}
