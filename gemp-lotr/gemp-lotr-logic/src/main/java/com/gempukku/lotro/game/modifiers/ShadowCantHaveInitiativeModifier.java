package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class ShadowCantHaveInitiativeModifier extends AbstractModifier {
    public ShadowCantHaveInitiativeModifier(PhysicalCard source, Condition condition) {
        super(source, "Shadow can't have initiative", null, condition, ModifierEffect.INITIATIVE_MODIFIER);
    }

    @Override
    public boolean shadowCanHaveInitiative(DefaultGame game) {
        return false;
    }
}
