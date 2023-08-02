package com.gempukku.lotro.modifiers.condition.lotronly;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.Condition;

public class CantSpotCondition implements Condition {
    private final Filterable[] _filters;

    public CantSpotCondition(Filterable... filters) {
        this._filters = filters;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return !Filters.canSpot(game, _filters);
    }
}
