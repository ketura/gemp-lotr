package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class CancelSkirmishEffect extends AbstractEffect {
    private Filter _involvementFilter;

    public CancelSkirmishEffect() {
    }

    public CancelSkirmishEffect(Filter involvementFilter) {
        _involvementFilter = involvementFilter;
    }

    @Override
    public String getText(LotroGame game) {
        return "Cancel skirmish";
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getSkirmish() != null
                && !game.getGameState().getSkirmish().isCancelled()
                && (_involvementFilter == null || Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmish(), _involvementFilter) > 0);
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
