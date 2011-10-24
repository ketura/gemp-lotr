package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.List;

public class PreventMinionBeingAssignedToCharacterModifier extends AbstractModifier {
    private Side _side;
    private Filterable _minionFilter;

    public PreventMinionBeingAssignedToCharacterModifier(PhysicalCard source, Side side, Filterable characterFilter, Filterable minionFilter) {
        super(source, "Is affected by assignment restriction", characterFilter, new ModifierEffect[]{ModifierEffect.ASSIGNMENT_MODIFIER});
        _side = side;
        _minionFilter = minionFilter;
    }

    @Override
    public boolean isValidAssignments(GameState gameState, Side side, ModifiersQuerying modifiersQuerying, PhysicalCard companion, List<PhysicalCard> minions) {
        if ((_side == null || side == _side) && Filters.filter(minions, gameState, modifiersQuerying, _minionFilter).size() > 0)
            return false;

        return true;
    }
}
