package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class CantBeAssignedToSkirmishModifier extends AbstractModifier {
    public CantBeAssignedToSkirmishModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Can't be assigned to skirmish", affectFilter, ModifierEffect.ASSIGNMENT_MODIFIER);
    }

    public CantBeAssignedToSkirmishModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't be assigned to skirmish", affectFilter, condition, ModifierEffect.ASSIGNMENT_MODIFIER);
    }

    @Override
    public boolean isPreventedFromBeingAssignedToSkirmish(DefaultGame game, Side sidePlayer, PhysicalCard card) {
        return true;
    }
}
