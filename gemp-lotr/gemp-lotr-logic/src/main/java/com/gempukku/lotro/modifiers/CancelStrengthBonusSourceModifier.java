package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;

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
