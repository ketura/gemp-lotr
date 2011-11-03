package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.*;

public class SpecialFlagModifier extends AbstractModifier {
    private Condition _condition;
    private ModifierFlag _modifierFlag;

    public SpecialFlagModifier(PhysicalCard source, ModifierFlag modifierFlag) {
        this(source, null, modifierFlag);
    }

    public SpecialFlagModifier(PhysicalCard source, Condition condition, ModifierFlag modifierFlag) {
        super(source, "Special flag set", null, ModifierEffect.SPECIAL_FLAG_MODIFIER);
        _condition = condition;
        _modifierFlag = modifierFlag;
    }

    @Override
    public boolean hasFlagActive(GameState gameState, ModifiersQuerying modifiersQuerying, ModifierFlag modifierFlag) {
        if (_condition == null || _condition.isFullfilled(gameState, modifiersQuerying))
            return modifierFlag == _modifierFlag;
        return false;
    }
}
