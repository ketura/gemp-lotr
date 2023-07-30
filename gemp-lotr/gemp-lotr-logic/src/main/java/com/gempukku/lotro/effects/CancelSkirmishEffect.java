package com.gempukku.lotro.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.effects.results.SkirmishCancelledResult;

public class CancelSkirmishEffect extends AbstractEffect {
    private Filterable[] _involvementFilter;

    public CancelSkirmishEffect() {
    }

    public CancelSkirmishEffect(Filterable... involvementFilter) {
        _involvementFilter = involvementFilter;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Cancel skirmish";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().getSkirmish() != null
                && !game.getGameState().getSkirmish().isCancelled()
                && (_involvementFilter == null || Filters.countActive(game, Filters.and(_involvementFilter, Filters.inSkirmish)) > 0)
                && game.getModifiersQuerying().canCancelSkirmish(game, game.getGameState().getSkirmish().getFellowshipCharacter())
                && (game.getFormat().canCancelRingBearerSkirmish() || Filters.countActive(game, Filters.ringBearer, Filters.inSkirmish) == 0);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            final LotroPhysicalCard fellowshipCharacter = game.getGameState().getSkirmish().getFellowshipCharacter();
            if (fellowshipCharacter != null)
                game.getActionsEnvironment().emitEffectResult(
                        new SkirmishCancelledResult(fellowshipCharacter));
            game.getGameState().sendMessage("Skirmish is cancelled");
            game.getGameState().getSkirmish().cancel();
            return new FullEffectResult(true);
        }
        game.getGameState().sendMessage("Skirmish could not be cancelled");
        return new FullEffectResult(false);
    }
}
