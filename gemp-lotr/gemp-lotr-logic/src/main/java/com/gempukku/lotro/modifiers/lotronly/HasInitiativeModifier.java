package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.condition.Condition;

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
