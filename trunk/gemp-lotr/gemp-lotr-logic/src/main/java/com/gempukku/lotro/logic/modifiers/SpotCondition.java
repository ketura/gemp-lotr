package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.GameState;

public class SpotCondition implements Condition {
    private Filterable[] _filter;
    private int _count;

    public SpotCondition(Filterable... filter) {
        this(1, Filters.and(filter));
    }

    public SpotCondition(int count, Filterable... filter) {
        _filter = filter;
        _count = count;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return Filters.countSpottable(gameState, modifiersQuerying, _filter) >= _count;
    }
}

