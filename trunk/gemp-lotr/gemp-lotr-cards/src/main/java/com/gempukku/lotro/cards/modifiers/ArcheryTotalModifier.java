package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class ArcheryTotalModifier extends AbstractModifier {
    private Side _side;
    private Condition _condition;
    private Evaluator _evaluator;

    public ArcheryTotalModifier(PhysicalCard source, Side side, int modifier) {
        this(source, side, null, modifier);
    }

    public ArcheryTotalModifier(PhysicalCard source, Side side, Condition condition, int modifier) {
        this(source, side, condition, new ConstantEvaluator(modifier));
    }

    public ArcheryTotalModifier(PhysicalCard source, Side side, Condition condition, Evaluator evaluator) {
        super(source, null, null, ModifierEffect.ARCHERY_MODIFIER);
        _side = side;
        _condition = condition;
        _evaluator = evaluator;
    }

    @Override
    public String getText(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        int modifier = _evaluator.evaluateExpression(gameState, modifiersQuerying, self);
        return ((_side == Side.FREE_PEOPLE) ? "Fellowship" : "Minion") + " archery total " + ((modifier < 0) ? modifier : ("+" + modifier));
    }

    @Override
    public int getArcheryTotalModifier(GameState gameState, ModifiersQuerying modifiersQuerying, Side side) {
        if (side == _side && ((_condition == null) || (_condition.isFullfilled(gameState, modifiersQuerying))))
            return _evaluator.evaluateExpression(gameState, modifiersQuerying, null);
        else
            return 0;
    }
}
