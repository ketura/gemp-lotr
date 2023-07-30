package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;

public class CantRemoveBurdensModifier extends AbstractModifier {
    private final Filter _sourceFilters;

    public CantRemoveBurdensModifier(LotroPhysicalCard source, Condition condition, Filterable... sourceFilters) {
        super(source, "Can't remove burdens", Filters.and(sourceFilters), condition, ModifierEffect.BURDEN_MODIFIER);
        _sourceFilters = Filters.and(sourceFilters);
    }

    @Override
    public boolean canRemoveBurden(DefaultGame game, LotroPhysicalCard source) {
        if (_sourceFilters.accepts(game, source))
            return false;
        return true;
    }
}
