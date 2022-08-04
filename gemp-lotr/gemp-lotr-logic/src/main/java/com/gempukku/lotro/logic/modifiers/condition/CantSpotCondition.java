package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;

public class CantSpotCondition implements Condition {
    private final Filterable[] _filters;

    public CantSpotCondition(Filterable... filters) {
        this._filters = filters;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        return !Filters.canSpot(game, _filters);
    }
}
