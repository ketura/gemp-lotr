package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.GameState;

public class SpotCondition implements Condition {
    private Filter _filter;
    private int _count;

    public SpotCondition(Filter... filter) {
        this(Filters.and(filter), 1);
    }

    public SpotCondition(Filter filter, int count) {
        _filter = filter;
        _count = count;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return Filters.countSpottable(gameState, modifiersQuerying, _filter) >= _count;
    }
}

