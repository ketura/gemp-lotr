package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class SpotEffect extends AbstractEffect {
    private int _count;
    private Filterable[] _filters;

    public SpotEffect(int count, Filterable... filters) {
        _count = count;
        _filters = filters;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), _count, _filters);
    }

    @Override
    public String getText(LotroGame game) {
        return "Spot cards";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            return new FullEffectResult(true, true);
        }
        return new FullEffectResult(false, false);
    }
}
