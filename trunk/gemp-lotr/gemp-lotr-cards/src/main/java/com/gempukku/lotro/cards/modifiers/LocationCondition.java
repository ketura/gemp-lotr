package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class LocationCondition implements Condition {
    private Filter _filter;

    public LocationCondition(Filter filter) {
        _filter = filter;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return _filter.accepts(gameState, modifiersQuerying, gameState.getCurrentSite());
    }
}
