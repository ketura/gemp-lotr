package com.gempukku.lotro.game.modifiers.lotronly;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.AbstractModifier;
import com.gempukku.lotro.game.modifiers.Condition;
import com.gempukku.lotro.game.modifiers.ModifierEffect;
import com.gempukku.lotro.game.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;

public class ArcheryTotalModifier extends AbstractModifier {
    private final Side _side;
    private final Evaluator _evaluator;

    public ArcheryTotalModifier(PhysicalCard source, Side side, int modifier) {
        this(source, side, null, modifier);
    }

    public ArcheryTotalModifier(PhysicalCard source, Side side, Condition condition, int modifier) {
        this(source, side, condition, new ConstantEvaluator(modifier));
    }

    public ArcheryTotalModifier(PhysicalCard source, Side side, Condition condition, Evaluator evaluator) {
        super(source, null, null, condition, ModifierEffect.ARCHERY_MODIFIER);
        _side = side;
        _evaluator = evaluator;
    }

    @Override
    public String getText(DefaultGame game, PhysicalCard self) {
        int modifier = _evaluator.evaluateExpression(game, self);
        return ((_side == Side.FREE_PEOPLE) ? "Fellowship" : "Minion") + " archery total " + ((modifier < 0) ? modifier : ("+" + modifier));
    }

    @Override
    public int getArcheryTotalModifier(DefaultGame game, Side side) {
        if (side == _side)
            return _evaluator.evaluateExpression(game, null);
        return 0;
    }
}
