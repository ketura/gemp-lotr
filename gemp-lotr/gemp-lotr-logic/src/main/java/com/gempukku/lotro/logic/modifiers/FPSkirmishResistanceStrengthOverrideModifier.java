package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class FPSkirmishResistanceStrengthOverrideModifier extends AbstractModifier {
    private static final Evaluator _resistanceEvaluator =
            new Evaluator() {
                @Override
                public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                    return game.getModifiersQuerying().getResistance(game, cardAffected);
                }
            };

    public FPSkirmishResistanceStrengthOverrideModifier(PhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, null, affectFilter, condition, ModifierEffect.SKIRMISH_STRENGTH_EVALUATOR_MODIFIER);
    }

    @Override
    public String getText(LotroGame game, PhysicalCard self) {
        return "Uses resistance instead of strength when resolving skirmish";
    }

    @Override
    public Evaluator getFpSkirmishStrengthOverrideEvaluator(LotroGame game, PhysicalCard fpCharacter) {
        return _resistanceEvaluator;
    }
}
