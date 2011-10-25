package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;

public class SpecialFlagModifier extends AbstractModifier {
    private ModifierFlag _modifierFlag;

    public SpecialFlagModifier(PhysicalCard source, ModifierFlag modifierFlag) {
        super(source, "Special flag set", null, ModifierEffect.SPECIAL_FLAG_MODIFIER);
        _modifierFlag = modifierFlag;
    }

    @Override
    public boolean hasFlagActive(ModifierFlag modifierFlag) {
        return modifierFlag == _modifierFlag;
    }
}
