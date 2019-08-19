package com.gempukku.lotro.logic.decisions;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;

public abstract class ForEachYouSpotDecision extends IntegerAwaitingDecision {
    private LotroGame _lotroGame;
    private Filterable[] _filters;
    private int _defaultValue;
    private int _max;

    protected ForEachYouSpotDecision(int id, String text, LotroGame lotroGame, int defaultValue, Filterable... filter) {
        super(id, text, 0, Filters.countActive(lotroGame, filter)
                + ((filter.length == 1) ? lotroGame.getModifiersQuerying().getSpotBonus(lotroGame, filter[0]) : 0));
        _lotroGame = lotroGame;
        _filters = filter;
        _defaultValue = defaultValue;
    }
}
