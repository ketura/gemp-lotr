package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class FreePeoplePlayerMayNotAssignCharacterModifier extends AbstractModifier {
    private Condition _condition;

    public FreePeoplePlayerMayNotAssignCharacterModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, null, affectFilter);
    }

    public FreePeoplePlayerMayNotAssignCharacterModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Free people player may not assign character to skirmish", affectFilter, null, ModifierEffect.ASSIGNMENT_MODIFIER);
        _condition = condition;
    }

    @Override
    public boolean canBeAssignedToSkirmish(GameState gameState, Side sidePlayer, ModifiersQuerying modifiersQuerying, PhysicalCard card) {
        if (sidePlayer == Side.FREE_PEOPLE
                && (_condition == null || _condition.isFullfilled(gameState, modifiersQuerying)))
            return false;
        return true;
    }
}
