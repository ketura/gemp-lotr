package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class LocationEvaluator implements Evaluator {
    private int defaultValue;
    private int locationValue;
    private Filter locationFilter;

    public LocationEvaluator(int defaultValue, int locationValue, Filterable... locationFilter) {
        this.defaultValue = defaultValue;
        this.locationValue = locationValue;
        this.locationFilter = Filters.and(locationFilter);
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
        return locationFilter.accepts(gameState, modifiersQuerying, gameState.getCurrentSite()) ? locationValue : defaultValue;
    }
}
