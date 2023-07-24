package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class HasInitiativeModifier extends AbstractModifier {
    private final Side _side;

    public HasInitiativeModifier(LotroPhysicalCard source, Condition condition, Side side) {
        super(source, side + " has initiative", null, condition, ModifierEffect.INITIATIVE_MODIFIER);
        _side = side;
    }

    @Override
    public Side hasInitiative(DefaultGame game) {
        return _side;
    }
}
