package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class ResistanceModifier extends AbstractModifier {
    private Evaluator _evaluator;

    public ResistanceModifier(PhysicalCard source, Filterable affectFilter, int modifier) {
        this(source, affectFilter, new ConstantEvaluator(modifier));
    }

    public ResistanceModifier(PhysicalCard source, Filterable affectFilter, Evaluator evaluator) {
        super(source, null, affectFilter, ModifierEffect.RESISTANCE_MODIFIER);
        _evaluator = evaluator;
    }

    public ResistanceModifier(PhysicalCard source, Filterable affectFilter, Condition condition, int modifier) {
        this(source, affectFilter, condition, new ConstantEvaluator(modifier));
    }

    public ResistanceModifier(PhysicalCard source, Filterable affectFilter, Condition condition, Evaluator evaluator) {
        super(source, null, affectFilter, condition, ModifierEffect.RESISTANCE_MODIFIER);
        _evaluator = evaluator;
    }

    @Override
    public String getText(LotroGame game, PhysicalCard self) {
        int modifier = _evaluator.evaluateExpression(game, self);
        return "Resistance " + ((modifier < 0) ? modifier : ("+" + modifier));
    }

    @Override
    public int getResistanceModifier(LotroGame game, PhysicalCard physicalCard) {
        return _evaluator.evaluateExpression(game, physicalCard);
    }
}
