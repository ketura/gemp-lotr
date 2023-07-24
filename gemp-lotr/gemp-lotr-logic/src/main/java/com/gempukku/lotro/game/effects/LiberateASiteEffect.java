package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.PlayConditions;
import com.gempukku.lotro.game.timing.results.DiscardCardsFromPlayResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LiberateASiteEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final String _liberatingPlayer;
    private final String _liberatedSiteController;

    public LiberateASiteEffect(LotroPhysicalCard source, String liberatingPlayer, String liberatedSiteController) {
        _source = source;
        _liberatingPlayer = liberatingPlayer;
        _liberatedSiteController = liberatedSiteController;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return PlayConditions.canLiberateASite(game, _source.getOwner(), _source, null);
    }

    @Override
    public String getText(DefaultGame game) {
        return "Liberate a site";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    private LotroPhysicalCard getSiteToLiberate(DefaultGame game) {
        int maxUnoccupiedSite = Integer.MAX_VALUE;
        for (String playerId : game.getGameState().getPlayerOrder().getAllPlayers())
            maxUnoccupiedSite = Math.min(maxUnoccupiedSite, game.getGameState().getPlayerPosition(playerId) - 1);

        for (int i = maxUnoccupiedSite; i >= 1; i--) {
            LotroPhysicalCard site = game.getGameState().getSite(i);
            if (_liberatedSiteController == null) {
                if (site != null && site.getCardController() != null
                        && !site.getCardController().equals(game.getGameState().getCurrentPlayerId()))
                    return site;
            } else {
                if (site != null && site.getCardController() != null && site.getCardController().equals(_liberatedSiteController))
                    return site;
            }
        }

        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (isPlayableInFull(game)) {
            LotroPhysicalCard siteToLiberate = getSiteToLiberate(game);
            if (siteToLiberate != null) {
                Set<LotroPhysicalCard> cardsToRemove = new HashSet<>();
                Set<LotroPhysicalCard> discardedCards = new HashSet<>();

                List<LotroPhysicalCard> stackedCards = game.getGameState().getStackedCards(siteToLiberate);
                cardsToRemove.addAll(stackedCards);

                List<LotroPhysicalCard> attachedCards = game.getGameState().getAttachedCards(siteToLiberate);
                cardsToRemove.addAll(attachedCards);
                discardedCards.addAll(attachedCards);

                game.getGameState().removeCardsFromZone(_source.getOwner(), cardsToRemove);
                for (LotroPhysicalCard removedCard : cardsToRemove)
                    game.getGameState().addCardToZone(game, removedCard, Zone.DISCARD);

                game.getGameState().loseControlOfCard(siteToLiberate, Zone.ADVENTURE_PATH);

                for (LotroPhysicalCard discardedCard : discardedCards)
                    game.getActionsEnvironment().emitEffectResult(new DiscardCardsFromPlayResult(null, null, discardedCard));

                game.getGameState().sendMessage(_liberatingPlayer + " liberated a " + GameUtils.getCardLink(siteToLiberate) + " using " + GameUtils.getCardLink(_source));

                liberatedSiteCallback(siteToLiberate);

                return new FullEffectResult(true);
            }
        }
        return new FullEffectResult(false);
    }

    public void liberatedSiteCallback(LotroPhysicalCard liberatedSite) {
    }
}
