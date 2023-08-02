package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.ModifierEffect;
import com.gempukku.lotro.modifiers.condition.Condition;

public class ShadowCantHaveInitiativeModifier extends AbstractModifier {
    public ShadowCantHaveInitiativeModifier(LotroPhysicalCard source, Condition condition) {
        super(source, "Shadow can't have initiative", null, condition, ModifierEffect.INITIATIVE_MODIFIER);
    }

    @Override
    public boolean shadowCanHaveInitiative(DefaultGame game) {
        return false;
    }
}
