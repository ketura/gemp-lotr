package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class AddNoTwilightForCompanionMoveModifier extends AbstractModifier {
    public AddNoTwilightForCompanionMoveModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Adds no twilight for move", affectFilter, null, ModifierEffect.MOVE_TWILIGHT_MODIFIER);
    }

    @Override
    public boolean addsTwilightForCompanionMove(LotroGame game, PhysicalCard companion) {
        return false;
    }
}
