package com.gempukku.lotro.game.decisions;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;

public abstract class ForEachYouSpotDecision extends IntegerAwaitingDecision {
    protected ForEachYouSpotDecision(int id, String text, DefaultGame lotroGame, Filterable... filter) {
        super(id, text, 0, Filters.countSpottable(lotroGame, filter), Filters.countSpottable(lotroGame, filter));
    }
}
