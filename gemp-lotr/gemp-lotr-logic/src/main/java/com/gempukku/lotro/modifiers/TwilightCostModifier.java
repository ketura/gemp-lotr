package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

public class TwilightCostModifier extends AbstractModifier {
    private final Evaluator _evaluator;

    public TwilightCostModifier(LotroPhysicalCard source, Filterable affectFilter, int twilightCostModifier) {
        this(source, affectFilter, null, twilightCostModifier);
    }

    public TwilightCostModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition, int twilightCostModifier) {
        this(source, affectFilter, condition, new ConstantEvaluator(twilightCostModifier));
    }

    public TwilightCostModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition, Evaluator evaluator) {
        super(source, null, affectFilter, condition, ModifierEffect.TWILIGHT_COST_MODIFIER);
        _evaluator = evaluator;
    }

    @Override
    public String getText(DefaultGame game, LotroPhysicalCard self) {
        final int value = _evaluator.evaluateExpression(game, self);
        if (value >= 0)
            return "Twilight cost +" + value;
        else
            return "Twilight cost " + value;
    }

    @Override
    public int getTwilightCostModifier(DefaultGame game, LotroPhysicalCard physicalCard, LotroPhysicalCard target, boolean ignoreRoamingPenalty) {
        return _evaluator.evaluateExpression(game, physicalCard);
    }
}
