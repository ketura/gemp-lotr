package com.gempukku.lotro.modifiers.condition;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.Condition;

public class CardStackedCondition implements Condition {
    private final Filter _stackedOn;
    private final Filter _cardStacked;
    private final int _minimumCount;

    public CardStackedCondition(int minimumCount, Filterable stackedOn, Filterable... cardStacked) {
        _minimumCount = minimumCount;
        _stackedOn = Filters.and(stackedOn);
        _cardStacked = Filters.and(cardStacked);
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return Filters.findFirstActive(game, _stackedOn, Filters.hasStacked(_minimumCount, _cardStacked)) != null;
    }
}
