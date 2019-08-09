package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CantAddBurdensModifier extends AbstractModifier {
    private Filter _sourceFilters;

    public CantAddBurdensModifier(PhysicalCard source, Condition condition, Filterable... sourceFilters) {
        super(source, "Can't add burdens", null, condition, ModifierEffect.BURDEN_MODIFIER);
        _sourceFilters = Filters.and(sourceFilters);
    }

    @Override
    public boolean canAddBurden(LotroGame game, String performingPlayer, PhysicalCard source) {
        if (_sourceFilters.accepts(game, source))
            return false;
        return true;
    }
}
