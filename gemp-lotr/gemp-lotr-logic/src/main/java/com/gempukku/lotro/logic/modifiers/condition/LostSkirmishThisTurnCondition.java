package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;

public class LostSkirmishThisTurnCondition implements Condition {
    private final Filter filter;

    public LostSkirmishThisTurnCondition(Filterable... filters) {
        filter = Filters.and(filters);
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        for (EffectResult effectResult : game.getActionsEnvironment().getTurnEffectResults()) {
            if (effectResult.getType() == EffectResult.Type.CHARACTER_LOST_SKIRMISH) {
                CharacterLostSkirmishResult lostResult = (CharacterLostSkirmishResult) effectResult;
                if (filter.accepts(game, lostResult.getLoser()))
                    return true;
            }
        }
        return false;
    }
}
