package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

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
    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
        return locationFilter.accepts(game, game.getGameState().getCurrentSite()) ? locationValue : defaultValue;
    }
}
