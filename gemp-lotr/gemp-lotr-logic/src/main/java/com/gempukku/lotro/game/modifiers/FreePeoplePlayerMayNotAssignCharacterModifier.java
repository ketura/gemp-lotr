package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class FreePeoplePlayerMayNotAssignCharacterModifier extends AbstractModifier {
    public FreePeoplePlayerMayNotAssignCharacterModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, null, affectFilter);
    }

    public FreePeoplePlayerMayNotAssignCharacterModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Free people player may not assign character to skirmish", affectFilter, condition, ModifierEffect.ASSIGNMENT_MODIFIER);
    }

    @Override
    public boolean isPreventedFromBeingAssignedToSkirmish(DefaultGame game, Side sidePlayer, PhysicalCard card) {
        if (sidePlayer == Side.FREE_PEOPLE)
            return true;
        return false;
    }
}
