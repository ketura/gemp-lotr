package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class TwilightCostModifier extends AbstractModifier {
    private Evaluator _evaluator;

    public TwilightCostModifier(PhysicalCard source, Filterable affectFilter, int twilightCostModifier) {
        this(source, affectFilter, null, twilightCostModifier);
    }

    public TwilightCostModifier(PhysicalCard source, Filterable affectFilter, Condition condition, int twilightCostModifier) {
        this(source, affectFilter, condition, new ConstantEvaluator(twilightCostModifier));
    }

    public TwilightCostModifier(PhysicalCard source, Filterable affectFilter, Condition condition, Evaluator evaluator) {
        super(source, null, affectFilter, condition, new ModifierEffect[]{ModifierEffect.TWILIGHT_COST_MODIFIER});
        _evaluator = evaluator;
    }

    @Override
    public String getText(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        final int value = _evaluator.evaluateExpression(gameState, modifiersQuerying, self);
        if (value >= 0)
            return "Twilight cost +" + value;
        else
            return "Twilight cost " + value;
    }

    @Override
    public int getTwilightCost(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, int result) {
        return result + _evaluator.evaluateExpression(gameState, modifiersLogic, physicalCard);
    }
}
