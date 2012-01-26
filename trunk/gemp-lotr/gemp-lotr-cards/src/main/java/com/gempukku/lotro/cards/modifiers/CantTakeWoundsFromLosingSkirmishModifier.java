package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Set;

public class CantTakeWoundsFromLosingSkirmishModifier extends AbstractModifier {
    private Filterable _winnersFilter;

    public CantTakeWoundsFromLosingSkirmishModifier(PhysicalCard source, Filterable affectFilter, Filterable winnersFilter) {
        super(source, "Can't take wounds", affectFilter, ModifierEffect.WOUND_MODIFIER);
        _winnersFilter = winnersFilter;
    }

    @Override
    public boolean canTakeWoundsFromLosingSkirmish(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Set<PhysicalCard> winners) {
        if (_winnersFilter == null
                || Filters.filter(winners, gameState, modifiersQuerying, _winnersFilter).size() > 0)
            return false;
        return true;
    }
}
