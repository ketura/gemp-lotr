package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;

public class GameHasCondition implements Condition {
    private Filterable[] _filter;
    private int _count;

    public GameHasCondition(Filterable... filter) {
        this(1, Filters.and(filter));
    }

    public GameHasCondition(int count, Filterable... filter) {
        _filter = filter;
        _count = count;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        return Filters.countActive(game, _filter)>=_count;
    }
}
