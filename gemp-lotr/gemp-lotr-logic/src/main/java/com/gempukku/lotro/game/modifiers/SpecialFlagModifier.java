package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class SpecialFlagModifier extends AbstractModifier {
    private final ModifierFlag _modifierFlag;

    public SpecialFlagModifier(PhysicalCard source, ModifierFlag modifierFlag) {
        this(source, null, modifierFlag);
    }

    public SpecialFlagModifier(PhysicalCard source, Condition condition, ModifierFlag modifierFlag) {
        super(source, "Special flag set", null, condition, ModifierEffect.SPECIAL_FLAG_MODIFIER);
        _modifierFlag = modifierFlag;
    }

    @Override
    public boolean hasFlagActive(DefaultGame game, ModifierFlag modifierFlag) {
        return modifierFlag == _modifierFlag;
    }
}
