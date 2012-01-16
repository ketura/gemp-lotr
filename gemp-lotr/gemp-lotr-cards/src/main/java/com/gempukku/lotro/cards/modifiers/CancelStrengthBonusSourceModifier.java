package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CancelStrengthBonusSourceModifier extends AbstractModifier {
    public CancelStrengthBonusSourceModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Cancel strength bonus", affectFilter, ModifierEffect.STRENGTH_BONUS_SOURCE_MODIFIER);
    }

    public CancelStrengthBonusSourceModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Cancel strength bonus", affectFilter, condition, ModifierEffect.STRENGTH_BONUS_SOURCE_MODIFIER);
    }

    @Override
    public boolean appliesStrengthBonusModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard modifierSource, PhysicalCard modifierTaget) {
        return false;
    }
}
