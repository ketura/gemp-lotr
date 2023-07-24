package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;

public class StrengthModifier extends AbstractModifier {
    private final Evaluator _evaluator;
    private final boolean _nonCardTextModifier;

    public StrengthModifier(LotroPhysicalCard source, Filterable affectFilter, int modifier) {
        this(source, affectFilter, null, modifier);
    }

    public StrengthModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition, int modifier) {
        this(source, affectFilter, condition, new ConstantEvaluator(modifier));
    }

    public StrengthModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition, Evaluator evaluator) {
        this(source, affectFilter, condition, evaluator, false);
    }

    public StrengthModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition, Evaluator evaluator, boolean nonCardTextModifier) {
        super(source, null, affectFilter, condition, ModifierEffect.STRENGTH_MODIFIER);
        _evaluator = evaluator;
        _nonCardTextModifier = nonCardTextModifier;
    }

    @Override
    public String getText(DefaultGame game, LotroPhysicalCard self) {
        final int value = _evaluator.evaluateExpression(game, self);
        if (value >= 0)
            return "Strength +" + value;
        else
            return "Strength " + value;
    }

    @Override
    public int getStrengthModifier(DefaultGame game, LotroPhysicalCard physicalCard) {
        return _evaluator.evaluateExpression(game, physicalCard);
    }

    @Override
    public boolean isNonCardTextModifier() {
        return _nonCardTextModifier;
    }
}
