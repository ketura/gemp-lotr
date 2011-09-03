package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantTakeWoundsModifier extends AbstractModifier {
    public CantTakeWoundsModifier(PhysicalCard source, Filter affectFilter) {
        super(source, "Can't take wounds", affectFilter, new ModifierEffect[]{ModifierEffect.WOUND_MODIFIER});
    }

    @Override
    public boolean canTakeWound(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, boolean result) {
        return false;
    }
}
