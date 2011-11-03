package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class HasInitiativeModifier extends AbstractModifier {
    private Condition _condition;
    private Side _side;

    public HasInitiativeModifier(PhysicalCard source, Condition condition, Side side) {
        super(source, side + " has initiative", null, ModifierEffect.INITIATIVE_MODIFIER);
        _condition = condition;
        _side = side;
    }

    @Override
    public Side hasInitiative(GameState gameState, ModifiersQuerying modifiersQuerying) {
        if (_condition == null || _condition.isFullfilled(gameState, modifiersQuerying))
            return _side;
        return null;
    }
}
