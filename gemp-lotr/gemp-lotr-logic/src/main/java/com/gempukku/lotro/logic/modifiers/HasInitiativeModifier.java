package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class HasInitiativeModifier extends AbstractModifier {
    private Side _side;

    public HasInitiativeModifier(PhysicalCard source, Condition condition, Side side) {
        super(source, side + " has initiative", null, condition, ModifierEffect.INITIATIVE_MODIFIER);
        _side = side;
    }

    @Override
    public Side hasInitiative(LotroGame game) {
        return _side;
    }
}
