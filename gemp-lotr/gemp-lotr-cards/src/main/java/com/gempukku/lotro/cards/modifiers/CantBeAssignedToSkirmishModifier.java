package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantBeAssignedToSkirmishModifier extends AbstractModifier {
    public CantBeAssignedToSkirmishModifier(PhysicalCard source, Filter affectFilter) {
        super(source, "Can't be assigned to skirmish", affectFilter, new ModifierEffect[]{ModifierEffect.ASSIGNMENT_MODIFIER});
    }

    @Override
    public boolean canBeAssignedToSkirmish(GameState gameState, Side sidePlayer, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return false;
    }
}
