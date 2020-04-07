package com.gempukku.lotro.logic.decisions;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;

public abstract class ForEachYouSpotDecision extends IntegerAwaitingDecision {
    protected ForEachYouSpotDecision(int id, String text, LotroGame lotroGame, Filterable... filter) {
        super(id, text, 0, Filters.countSpottable(lotroGame, filter), Filters.countSpottable(lotroGame, filter));
    }
}
