package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;

public class CancelStrengthBonusModifier extends AbstractModifier {
    public CancelStrengthBonusModifier(PhysicalCard source, Filter affectFilter) {
        super(source, "Cancel strength bonus", affectFilter);
    }

    @Override
    public boolean appliesStrengthModifier(GameState gameState, ModifiersLogic modifiersLogic, PhysicalCard modifierSource, boolean result) {
        return false;
    }
}
