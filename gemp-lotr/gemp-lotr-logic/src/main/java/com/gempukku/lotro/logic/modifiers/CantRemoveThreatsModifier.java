package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CantRemoveThreatsModifier extends AbstractModifier {
    private Filter _sourceFilters;

    public CantRemoveThreatsModifier(PhysicalCard source, Condition condition, Filterable... sourceFilters) {
        super(source, "Can't remove threats", null, condition, ModifierEffect.THREAT_MODIFIER);
        _sourceFilters = Filters.and(sourceFilters);
    }

    @Override
    public boolean canRemoveThreat(LotroGame game, PhysicalCard source) {
        if (_sourceFilters.accepts(game, source))
            return false;
        return true;
    }
}
