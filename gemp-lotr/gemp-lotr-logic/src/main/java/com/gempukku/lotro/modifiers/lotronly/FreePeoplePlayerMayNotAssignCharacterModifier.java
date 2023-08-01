package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.condition.Condition;

public class FreePeoplePlayerMayNotAssignCharacterModifier extends AbstractModifier {
    public FreePeoplePlayerMayNotAssignCharacterModifier(LotroPhysicalCard source, Filterable affectFilter) {
        this(source, null, affectFilter);
    }

    public FreePeoplePlayerMayNotAssignCharacterModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Free people player may not assign character to skirmish", affectFilter, condition, ModifierEffect.ASSIGNMENT_MODIFIER);
    }

    @Override
    public boolean isPreventedFromBeingAssignedToSkirmish(DefaultGame game, Side sidePlayer, LotroPhysicalCard card) {
        if (sidePlayer == Side.FREE_PEOPLE)
            return true;
        return false;
    }
}
