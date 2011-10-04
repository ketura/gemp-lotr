package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ArcheryTotalModifier extends AbstractModifier {
    private Side _side;
    private Condition _condition;
    private int _modifier;

    public ArcheryTotalModifier(PhysicalCard source, Side side, int modifier) {
        this(source, side, null, modifier);
    }

    public ArcheryTotalModifier(PhysicalCard source, Side side, Condition condition, int modifier) {
        super(source, ((side == Side.FREE_PEOPLE) ? "Fellowship" : "Minion") + " archery total " + ((modifier < 0) ? modifier : ("+" + modifier)), null, new ModifierEffect[]{ModifierEffect.ARCHERY_MODIFIER});
        _side = side;
        _condition = condition;
        _modifier = modifier;
    }

    @Override
    public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, Side side, int result) {
        if (side == _side && ((_condition == null) || (_condition.isFullfilled(gameState, modifiersQuerying))))
            return result + _modifier;
        else
            return result;
    }
}
