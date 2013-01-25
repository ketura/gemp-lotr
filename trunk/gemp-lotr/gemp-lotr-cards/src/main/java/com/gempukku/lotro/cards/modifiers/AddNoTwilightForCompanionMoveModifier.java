package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;

public class AddNoTwilightForCompanionMoveModifier extends AbstractModifier {
    public AddNoTwilightForCompanionMoveModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Adds no twilight for move", affectFilter, null, ModifierEffect.MOVE_TWILIGHT_MODIFIER);
    }

    @Override
    public boolean addsTwilightForCompanionMove(GameState gameState, ModifiersLogic modifiersLogic, PhysicalCard companion) {
        return false;
    }
}
