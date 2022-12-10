package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class FPSkirmishVitalityStrengthOverrideModifier extends AbstractModifier {
    private static final Evaluator _vitalityEvaluator =
            (game, cardAffected) -> game.getModifiersQuerying().getVitality(game, cardAffected);

    public FPSkirmishVitalityStrengthOverrideModifier(PhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, null, affectFilter, condition, ModifierEffect.SKIRMISH_STRENGTH_EVALUATOR_MODIFIER);
    }

    @Override
    public String getText(LotroGame game, PhysicalCard self) {
        return "Uses vitality instead of strength when resolving skirmish";
    }

    @Override
    public Evaluator getFpSkirmishStrengthOverrideEvaluator(LotroGame game, PhysicalCard fpCharacter) {
        return _vitalityEvaluator;
    }
}
