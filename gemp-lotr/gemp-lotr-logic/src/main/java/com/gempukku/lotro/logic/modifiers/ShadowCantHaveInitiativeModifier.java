package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class ShadowCantHaveInitiativeModifier extends AbstractModifier {
    public ShadowCantHaveInitiativeModifier(PhysicalCard source, Condition condition) {
        super(source, "Shadow can't have initiative", null, condition, ModifierEffect.INITIATIVE_MODIFIER);
    }

    @Override
    public boolean shadowCanHaveInitiative(LotroGame game) {
        return false;
    }
}
