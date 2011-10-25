package com.gempukku.lotro.cards.modifiers.conditions;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantSpotCondition implements Condition {
    private Filterable[] _filters;

    public CantSpotCondition(Filterable... filters) {
        this._filters = filters;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return !Filters.canSpot(gameState, modifiersQuerying, _filters);
    }
}
