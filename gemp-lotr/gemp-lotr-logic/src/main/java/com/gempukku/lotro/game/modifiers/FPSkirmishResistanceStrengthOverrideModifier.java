package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;

public class FPSkirmishResistanceStrengthOverrideModifier extends AbstractModifier {
    private static final Evaluator _resistanceEvaluator =
            new Evaluator() {
                @Override
                public int evaluateExpression(DefaultGame game, PhysicalCard cardAffected) {
                    return game.getModifiersQuerying().getResistance(game, cardAffected);
                }
            };

    public FPSkirmishResistanceStrengthOverrideModifier(PhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, null, affectFilter, condition, ModifierEffect.SKIRMISH_STRENGTH_EVALUATOR_MODIFIER);
    }

    @Override
    public String getText(DefaultGame game, PhysicalCard self) {
        return "Uses resistance instead of strength when resolving skirmish";
    }

    @Override
    public Evaluator getFpSkirmishStrengthOverrideEvaluator(DefaultGame game, PhysicalCard fpCharacter) {
        return _resistanceEvaluator;
    }
}
