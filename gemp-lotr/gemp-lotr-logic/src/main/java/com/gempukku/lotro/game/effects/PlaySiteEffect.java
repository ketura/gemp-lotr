package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.actions.SubAction;
import com.gempukku.lotro.game.modifiers.ModifierFlag;
import com.gempukku.lotro.game.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.game.timing.AbstractEffect;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.timing.Effect;
import com.gempukku.lotro.game.timing.results.PlayCardResult;
import com.gempukku.lotro.game.timing.results.ReplaceSiteResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PlaySiteEffect extends AbstractEffect {
    private final Action _action;
    private final String _playerId;
    private final SitesBlock _siteBlock;
    private final int _siteNumber;
    private final Filterable[] _extraSiteFilters;

    public PlaySiteEffect(Action action, String playerId, SitesBlock siteBlock, int siteNumber) {
        this(action, playerId, siteBlock, siteNumber, Filters.any);
    }

    public PlaySiteEffect(Action action, String playerId, SitesBlock siteBlock, int siteNumber, Filterable... extraSiteFilters) {
        _action = action;
        _playerId = playerId;
        _siteBlock = siteBlock;
        _siteNumber = siteNumber;
        _extraSiteFilters = extraSiteFilters;
    }

    protected int getSiteNumberToPlay(DefaultGame game) {
        return _siteNumber;
    }

    private Collection<PhysicalCard> getMatchingSites(DefaultGame game) {
        final int siteNumber = getSiteNumberToPlay(game);

        if (siteNumber > 9 || siteNumber < 1)
            return Collections.emptySet();

        if (game.getFormat().isOrderedSites()) {
            Filter printedSiteNumber = new Filter() {
                @Override
                public boolean accepts(DefaultGame game, PhysicalCard physicalCard) {
                    return physicalCard.getBlueprint().getSiteNumber() == siteNumber;
                }
            };
            if (_siteBlock != null)
                return Filters.filter(game.getGameState().getAdventureDeck(_playerId), game, Filters.and(_extraSiteFilters, printedSiteNumber, Filters.siteBlock(_siteBlock)));
            else
                return Filters.filter(game.getGameState().getAdventureDeck(_playerId), game, Filters.and(_extraSiteFilters, printedSiteNumber));
        } else {
            if (_siteBlock != null)
                return Filters.filter(game.getGameState().getAdventureDeck(_playerId), game, Filters.and(_extraSiteFilters, Filters.siteBlock(_siteBlock)));
            else
                return Filters.filter(game.getGameState().getAdventureDeck(_playerId), game, _extraSiteFilters);
        }
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return getMatchingSites(game).size() > 0;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Play site";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        final int siteNumber = getSiteNumberToPlay(game);

        Collection<PhysicalCard> newSite = getMatchingSites(game);

        PhysicalCard currentSite = game.getGameState().getSite(siteNumber);

        if (newSite.size() > 0 && (currentSite == null || game.getModifiersQuerying().canReplaceSite(game, _playerId, currentSite))
                && game.getModifiersQuerying().canPlaySite(game, _playerId)) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(
                    new ChooseArbitraryCardsEffect(_playerId, "Choose site to play", newSite, 1, 1) {
                        @Override
                        protected void cardsSelected(DefaultGame game, Collection<PhysicalCard> selectedCards) {
                            PhysicalCard newSite = selectedCards.iterator().next();

                            GameState gameState = game.getGameState();
                            PhysicalCard oldSite = gameState.getSite(siteNumber);

                            Zone zone = null;
                            String controlled = null;
                            List<PhysicalCard> stacked = null;

                            if (oldSite != null) {
                                controlled = oldSite.getCardController();
                                if (controlled != null)
                                    zone = oldSite.getZone();
                                stacked = new LinkedList<>(gameState.getStackedCards(oldSite));

                                gameState.removeCardsFromZone(_playerId, Collections.singleton(oldSite));
                                oldSite.setSiteNumber(null);
                                gameState.addCardToZone(game, oldSite, Zone.ADVENTURE_DECK);
                                if (gameState.getCurrentSiteNumber() == siteNumber
                                        && !_playerId.equals(gameState.getCurrentPlayerId()))
                                    game.getModifiersEnvironment().addUntilEndOfTurnModifier(
                                            new SpecialFlagModifier(null, ModifierFlag.SHADOW_PLAYER_REPLACED_CURRENT_SITE));

                                game.getActionsEnvironment().emitEffectResult(new ReplaceSiteResult(_playerId, siteNumber));
                            }

                            gameState.removeCardsFromZone(_playerId, Collections.singleton(newSite));
                            newSite.setSiteNumber(siteNumber);
                            gameState.addCardToZone(game, newSite, Zone.ADVENTURE_PATH);
                            gameState.sendMessage(newSite.getOwner() + " plays " + GameUtils.getCardLink(newSite));

                            if (controlled != null) {
                                gameState.takeControlOfCard(controlled, game, newSite, zone);
                                for (PhysicalCard physicalCard : stacked)
                                    gameState.stackCard(game, physicalCard, newSite);
                            }

                            sitePlayedCallback(newSite);
                            game.getActionsEnvironment().emitEffectResult(new PlayCardResult(Zone.ADVENTURE_DECK, newSite, null, null, false));
                        }
                    });

            game.getActionsEnvironment().addActionToStack(subAction);
            return new FullEffectResult(true);
        } else if (newSite.size() > 0) {
            game.getGameState().sendMessage("Can't play a site");
            return new FullEffectResult(false);
        } else {
            game.getGameState().sendMessage("Can't find a matching site to play in " + _playerId + " adventure deck");
            return new FullEffectResult(false);
        }
    }

    protected void sitePlayedCallback(PhysicalCard site) {

    }
}
