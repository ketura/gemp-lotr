package com.gempukku.lotro.modifiers.condition;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.Condition;
import com.gempukku.lotro.effects.EffectResult;
import com.gempukku.lotro.effects.results.PlayCardResult;

public class CardPlayedInCurrentPhaseCondition implements Condition {
    private final Filter filter;

    public CardPlayedInCurrentPhaseCondition(Filterable... filters) {
        filter = Filters.and(filters);
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        for (EffectResult effectResult : game.getActionsEnvironment().getPhaseEffectResults()) {
            if (effectResult instanceof PlayCardResult playResult) {
                if (filter.accepts(game, playResult.getPlayedCard()))
                    return true;
            }
        }

        return false;

    }
}
