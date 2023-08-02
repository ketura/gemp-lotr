package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.condition.Condition;
import com.gempukku.lotro.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

public class InitiativeHandSizeModifier extends AbstractModifier {
    private final Evaluator _evaluator;

    public InitiativeHandSizeModifier(LotroPhysicalCard source, int modifier) {
        this(source, null, modifier);
    }

    public InitiativeHandSizeModifier(LotroPhysicalCard source, Condition condition, int modifier) {
        this(source, condition, new ConstantEvaluator(modifier));
    }

    public InitiativeHandSizeModifier(LotroPhysicalCard source, Condition condition, Evaluator evaluator) {
        super(source, null, null, condition, ModifierEffect.INITIATIVE_MODIFIER);
        _evaluator = evaluator;
    }

    @Override
    public int getInitiativeHandSizeModifier(DefaultGame game) {
        return _evaluator.evaluateExpression(game, null);
    }
}
