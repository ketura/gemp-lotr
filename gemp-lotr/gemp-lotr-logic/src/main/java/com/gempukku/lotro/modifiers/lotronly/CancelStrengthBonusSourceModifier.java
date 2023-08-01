package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.condition.Condition;

public class CancelStrengthBonusSourceModifier extends AbstractModifier {
    public CancelStrengthBonusSourceModifier(LotroPhysicalCard source, Filterable affectFilter) {
        super(source, "Cancel strength bonus", affectFilter, ModifierEffect.STRENGTH_BONUS_SOURCE_MODIFIER);
    }

    public CancelStrengthBonusSourceModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Cancel strength bonus", affectFilter, condition, ModifierEffect.STRENGTH_BONUS_SOURCE_MODIFIER);
    }

    @Override
    public boolean appliesStrengthBonusModifier(DefaultGame game, LotroPhysicalCard modifierSource, LotroPhysicalCard modifierTaget) {
        return false;
    }
}
