package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.GameState;

public class SpotCondition implements Condition {
    private Filter _filter;

    public SpotCondition(Filter filter) {
        _filter = filter;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return Filters.canSpot(gameState, modifiersQuerying, _filter);
    }
}

