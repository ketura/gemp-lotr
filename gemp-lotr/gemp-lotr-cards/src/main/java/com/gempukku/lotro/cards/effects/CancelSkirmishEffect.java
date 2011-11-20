package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class CancelSkirmishEffect extends AbstractEffect {
    private Filterable[] _involvementFilter;

    public CancelSkirmishEffect() {
    }

    public CancelSkirmishEffect(Filterable... involvementFilter) {
        _involvementFilter = involvementFilter;
    }

    @Override
    public String getText(LotroGame game) {
        return "Cancel skirmish";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getSkirmish() != null
                && !game.getGameState().getSkirmish().isCancelled()
                && (_involvementFilter == null || Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.and(_involvementFilter, Filters.inSkirmish)) > 0)
                && (game.getFormat().canCancelRingBearerSkirmish() || !Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Keyword.RING_BEARER, Filters.inSkirmish));
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            game.getGameState().sendMessage("Skirmish is cancelled");
            game.getGameState().getSkirmish().cancel();
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
