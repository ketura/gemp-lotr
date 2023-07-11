package com.gempukku.lotro.game.modifiers.condition;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.Condition;

public class LocationCondition implements Condition {
    private final Filter _filter;

    public LocationCondition(Filterable... filters) {
        _filter = Filters.and(filters);
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return _filter.accepts(game, game.getGameState().getCurrentSite());
    }
}
