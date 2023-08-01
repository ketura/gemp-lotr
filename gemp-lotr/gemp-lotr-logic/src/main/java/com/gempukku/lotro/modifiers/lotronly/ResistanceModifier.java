package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.condition.Condition;
import com.gempukku.lotro.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

public class ResistanceModifier extends AbstractModifier {
    private final Evaluator evaluator;
    private final boolean nonCardTextModifier;

    public ResistanceModifier(LotroPhysicalCard source, Filterable affectFilter, int modifier) {
        this(source, affectFilter, new ConstantEvaluator(modifier));
    }

    public ResistanceModifier(LotroPhysicalCard source, Filterable affectFilter, Evaluator evaluator) {
        this(source, affectFilter, null, evaluator);
    }

    public ResistanceModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition, int modifier) {
        this(source, affectFilter, condition, new ConstantEvaluator(modifier));
    }

    public ResistanceModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition, Evaluator evaluator) {
        this(source, affectFilter, condition, evaluator, false);
    }

    public ResistanceModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition, Evaluator evaluator, boolean nonCardTextModifier) {
        super(source, null, affectFilter, condition, ModifierEffect.RESISTANCE_MODIFIER);
        this.evaluator = evaluator;
        this.nonCardTextModifier = nonCardTextModifier;
    }

    @Override
    public boolean isNonCardTextModifier() {
        return nonCardTextModifier;
    }

    @Override
    public String getText(DefaultGame game, LotroPhysicalCard self) {
        int modifier = evaluator.evaluateExpression(game, self);
        return "Resistance " + ((modifier < 0) ? modifier : ("+" + modifier));
    }

    @Override
    public int getResistanceModifier(DefaultGame game, LotroPhysicalCard physicalCard) {
        return evaluator.evaluateExpression(game, physicalCard);
    }
}
