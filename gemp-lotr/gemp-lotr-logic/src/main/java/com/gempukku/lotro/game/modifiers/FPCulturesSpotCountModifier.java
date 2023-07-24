package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;

public class FPCulturesSpotCountModifier extends AbstractModifier {
    private final Evaluator _valueEvaluator;

    public FPCulturesSpotCountModifier(LotroPhysicalCard source, int value) {
        this(source, null, new ConstantEvaluator(value));
    }

    public FPCulturesSpotCountModifier(LotroPhysicalCard source, Condition condition, int value) {
        this(source, condition, new ConstantEvaluator(value));
    }

    public FPCulturesSpotCountModifier(LotroPhysicalCard source, Condition condition, Evaluator value) {
        super(source, "Modifies FP culture count", null, condition, ModifierEffect.SPOT_MODIFIER);
        _valueEvaluator = value;
    }

    @Override
    public int getFPCulturesSpotCountModifier(DefaultGame game, String playerId) {
        return _valueEvaluator.evaluateExpression(game, null);
    }
}
