package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;

public class HasToMoveIfPossibleModifier extends AbstractModifier {
    public HasToMoveIfPossibleModifier(PhysicalCard source) {
        super(source, "Has to move if possible", null, new ModifierEffect[]{ModifierEffect.MOVE_LIMIT_MODIFIER});
    }

    @Override
    public boolean hasToMoveIfPossible() {
        return true;
    }
}
