package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ArcheryTotalModifier extends AbstractModifier {
    private Side _side;
    private int _modifier;

    public ArcheryTotalModifier(PhysicalCard source, Side side, int modifier) {
        super(source, ((side == Side.FREE_PEOPLE) ? "Fellowship" : "Minion") + " archery total " + ((modifier < 0) ? modifier : ("+" + modifier)), null);
        _side = side;
        _modifier = modifier;
    }

    @Override
    public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersQuerying, Side side, int result) {
        if (side == _side)
            return result + _modifier;
        else
            return result;
    }
}
