package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CardStackedCondition implements Condition {
    private Filter _stackedOn;
    private Filter _cardStacked;
    private int _minimumCount;

    public CardStackedCondition(int minimumCount, Filterable stackedOn, Filterable... cardStacked) {
        _minimumCount = minimumCount;
        _stackedOn = Filters.and(stackedOn);
        _cardStacked = Filters.and(cardStacked);
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return Filters.findFirstActive(gameState, modifiersQuerying, _stackedOn, Filters.hasStacked(_minimumCount, _cardStacked)) != null;
    }
}
