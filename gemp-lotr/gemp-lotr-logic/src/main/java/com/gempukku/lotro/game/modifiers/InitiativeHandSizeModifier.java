package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;

public class InitiativeHandSizeModifier extends AbstractModifier {
    private final Evaluator _evaluator;

    public InitiativeHandSizeModifier(PhysicalCard source, int modifier) {
        this(source, null, modifier);
    }

    public InitiativeHandSizeModifier(PhysicalCard source, Condition condition, int modifier) {
        this(source, condition, new ConstantEvaluator(modifier));
    }

    public InitiativeHandSizeModifier(PhysicalCard source, Condition condition, Evaluator evaluator) {
        super(source, null, null, condition, ModifierEffect.INITIATIVE_MODIFIER);
        _evaluator = evaluator;
    }

    @Override
    public int getInitiativeHandSizeModifier(DefaultGame game) {
        return _evaluator.evaluateExpression(game, null);
    }
}
