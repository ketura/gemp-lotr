package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantTakeWoundsModifier extends AbstractModifier {
    public CantTakeWoundsModifier(PhysicalCard source, String text, Filter affectFilter) {
        super(source, text, affectFilter);
    }

    @Override
    public boolean canTakeWound(GameState gameState, ModifiersQuerying modifiersLogic, PhysicalCard physicalCard, boolean result) {
        return false;
    }
}
