package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.Condition;

public class SpotCondition implements Condition {
    private final Filterable[] _filter;
    private final int _count;

    public SpotCondition(Filterable... filter) {
        this(1, Filters.and(filter));
    }

    public SpotCondition(int count, Filterable... filter) {
        _filter = filter;
        _count = count;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return Filters.canSpot(game, _count, _filter);
    }
}

