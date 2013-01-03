package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;

public class CantBeAssignedToSkirmishModifier extends AbstractModifier {
    public CantBeAssignedToSkirmishModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Can't be assigned to skirmish", affectFilter, ModifierEffect.ASSIGNMENT_MODIFIER);
    }

    public CantBeAssignedToSkirmishModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be assigned to skirmish", affectFilter, condition, ModifierEffect.ASSIGNMENT_MODIFIER);
    }

    @Override
    public boolean isPreventedFromBeingAssignedToSkirmish(GameState gameState, Side sidePlayer, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        return true;
    }
}
