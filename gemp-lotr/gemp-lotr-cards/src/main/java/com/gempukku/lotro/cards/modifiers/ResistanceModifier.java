package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
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

    @Override
    public String getText(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        int modifier = _evaluator.evaluateExpression(gameState, modifiersQuerying, self);
        return "Resistance " + ((modifier < 0) ? modifier : ("+" + modifier));
    }

    @Override
    public int getResistanceModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return _evaluator.evaluateExpression(gameState, modifiersQuerying, physicalCard);
    }
}
