package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.List;

public class PreventMinionBeingAssignedToCompanionModifier extends AbstractModifier {
    private Filter _minionFilter;

    public PreventMinionBeingAssignedToCompanionModifier(PhysicalCard source, Filter companionFilter, Filter minionFilter) {
        super(source, "Is affected by assignment restriction", companionFilter);
        _minionFilter = minionFilter;
    }

    @Override
    public boolean isValidFreePlayerAssignments(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard companion, List<PhysicalCard> minions, boolean result) {
        if (Filters.filter(minions, gameState, modifiersQuerying, _minionFilter).size() > 0)
            return false;

        return result;
    }
}
