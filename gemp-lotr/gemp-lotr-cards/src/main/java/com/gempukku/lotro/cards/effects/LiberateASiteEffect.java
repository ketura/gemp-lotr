package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class LiberateASiteEffect extends AbstractEffect {
    private PhysicalCard _source;

    public LiberateASiteEffect(PhysicalCard source) {
        _source = source;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlledByShadowPlayer(game.getGameState().getCurrentPlayerId())) > 0;
    }

    @Override
    public String getText(LotroGame game) {
        return "Liberate a site";
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    private PhysicalCard getSiteToLiberate(LotroGame game) {
        int maxUnoccupiedSite = Integer.MAX_VALUE;
        for (String playerId : game.getGameState().getPlayerOrder().getAllPlayers())
            maxUnoccupiedSite = Math.min(maxUnoccupiedSite, game.getGameState().getPlayerPosition(playerId) - 1);

        for (int i = maxUnoccupiedSite; i >= 1; i++) {
            PhysicalCard site = game.getGameState().getSite(i);
            if (site.getCardController() != null
                    && !site.getCardController().equals(game.getGameState().getCurrentPlayerId()))
                return site;
        }

        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        PhysicalCard siteToLiberate = getSiteToLiberate(game);
        if (siteToLiberate != null) {

            List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(siteToLiberate);
            for (PhysicalCard stackedCard : stackedCards) {
                game.getGameState().removeCardFromZone(stackedCard);
                game.getGameState().addCardToZone(stackedCard, Zone.DISCARD);
            }

            game.getGameState().loseControlOfCard(siteToLiberate, Zone.ADVENTURE_PATH);

            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
