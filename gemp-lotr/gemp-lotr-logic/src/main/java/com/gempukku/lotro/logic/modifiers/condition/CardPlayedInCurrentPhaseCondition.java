package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

public class CardPlayedInCurrentPhaseCondition implements Condition {
    private final Filter filter;

    public CardPlayedInCurrentPhaseCondition(Filterable... filters) {
        filter = Filters.and(filters);
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        for (EffectResult effectResult : game.getActionsEnvironment().getPhaseEffectResults()) {
            if (effectResult instanceof PlayCardResult playResult) {
                if (filter.accepts(game, playResult.getPlayedCard()))
                    return true;
            }
        }

        return false;

    }
}
