package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class AddNoTwilightForCompanionMoveModifier extends AbstractModifier {
    public AddNoTwilightForCompanionMoveModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Adds no twilight for move", affectFilter, null, ModifierEffect.MOVE_TWILIGHT_MODIFIER);
    }

    @Override
    public boolean addsTwilightForCompanionMove(DefaultGame game, PhysicalCard companion) {
        return false;
    }
}
