package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class LocationCondition implements Condition {
    private Filter _filter;

    public LocationCondition(Filterable... filters) {
        _filter = Filters.and(filters);
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return _filter.accepts(gameState, modifiersQuerying, gameState.getCurrentSite());
    }
}
