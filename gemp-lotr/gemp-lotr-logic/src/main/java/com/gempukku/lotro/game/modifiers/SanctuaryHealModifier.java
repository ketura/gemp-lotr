package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;

public class SanctuaryHealModifier extends AbstractModifier {
    private final Evaluator evaluator;

    public SanctuaryHealModifier(PhysicalCard source, Condition condition, Evaluator amount) {
        super(source, "Sanctuary heal modifier "+amount, null, condition, ModifierEffect.SANCTUARY_HEAL_MODIFIER);
        evaluator = amount;
    }

    public SanctuaryHealModifier(PhysicalCard source, Condition condition, int amount) {
        super(source, "Sanctuary heal modifier "+amount, null, condition, ModifierEffect.SANCTUARY_HEAL_MODIFIER);
        evaluator = new ConstantEvaluator(amount);
    }

    @Override
    public int getSanctuaryHealModifier(DefaultGame game) {
        return evaluator.evaluateExpression(game, null);
    }
}
