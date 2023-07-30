package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class SpecialFlagModifier extends AbstractModifier {
    private final ModifierFlag _modifierFlag;

    public SpecialFlagModifier(LotroPhysicalCard source, ModifierFlag modifierFlag) {
        this(source, null, modifierFlag);
    }

    public SpecialFlagModifier(LotroPhysicalCard source, Condition condition, ModifierFlag modifierFlag) {
        super(source, "Special flag set", null, condition, ModifierEffect.SPECIAL_FLAG_MODIFIER);
        _modifierFlag = modifierFlag;
    }

    @Override
    public boolean hasFlagActive(DefaultGame game, ModifierFlag modifierFlag) {
        return modifierFlag == _modifierFlag;
    }
}
