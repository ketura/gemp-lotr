package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class CancelStrengthBonusSourceModifier extends AbstractModifier {
    public CancelStrengthBonusSourceModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Cancel strength bonus", affectFilter, ModifierEffect.STRENGTH_BONUS_SOURCE_MODIFIER);
    }

    public CancelStrengthBonusSourceModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Cancel strength bonus", affectFilter, condition, ModifierEffect.STRENGTH_BONUS_SOURCE_MODIFIER);
    }

    @Override
    public boolean appliesStrengthBonusModifier(DefaultGame game, PhysicalCard modifierSource, PhysicalCard modifierTaget) {
        return false;
    }
}
