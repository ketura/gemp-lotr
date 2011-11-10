package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PlaySiteEffect extends AbstractEffect {
    private String _playerId;
    private Block _siteBlock;
    private int _siteNumber;
    private Filterable[] _extraSiteFilters;

    public PlaySiteEffect(String playerId, Block siteBlock, int siteNumber) {
        this(playerId, siteBlock, siteNumber, Filters.any);
    }

    public PlaySiteEffect(String playerId, Block siteBlock, int siteNumber, Filterable... extraSiteFilters) {
        _playerId = playerId;
        _siteBlock = siteBlock;
        _siteNumber = siteNumber;
        _extraSiteFilters = extraSiteFilters;
    }

    private PhysicalCard getMatchingSite(LotroGame game) {
        Collection<PhysicalCard> matching;
        if (_siteBlock != null) {
            matching = Filters.filter(game.getGameState().getAdventureDeck(_playerId), game.getGameState(), game.getModifiersQuerying(), Filters.and(_extraSiteFilters, Filters.siteNumber(_siteNumber), Filters.siteBlock(_siteBlock)));
        } else {
            matching = Filters.filter(game.getGameState().getAdventureDeck(_playerId), game.getGameState(), game.getModifiersQuerying(), Filters.and(_extraSiteFilters, Filters.siteNumber(_siteNumber)));
        }
        if (matching.size() > 0)
            return matching.iterator().next();
        else
            return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return getMatchingSite(game) != null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Play site";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        GameState gameState = game.getGameState();
        PhysicalCard newSite = getMatchingSite(game);
        if (newSite != null) {
            PhysicalCard card = gameState.getSite(_siteNumber);

            Zone zone = null;
            String controlled = null;
            List<PhysicalCard> stacked = null;

            if (card != null) {
                controlled = card.getCardController();
                if (controlled != null)
                    zone = card.getZone();
                stacked = new LinkedList<PhysicalCard>(gameState.getStackedCards(card));

                gameState.removeCardsFromZone(_playerId, Collections.singleton(card));
                gameState.addCardToZone(game, card, Zone.DECK);
            }

            gameState.removeCardsFromZone(_playerId, Collections.singleton(newSite));
            gameState.addCardToZone(game, newSite, Zone.ADVENTURE_PATH);
            gameState.sendMessage(newSite.getOwner() + " plays " + GameUtils.getCardLink(newSite));

            if (controlled != null) {
                gameState.takeControlOfCard(controlled, newSite, zone);
                for (PhysicalCard physicalCard : stacked)
                    gameState.stackCard(game, physicalCard, newSite);
            }

            sitePlayedCallback(newSite);
            return new FullEffectResult(Collections.singleton(new PlayCardResult(Zone.DECK, newSite, null, null)), true, true);
        }
        return new FullEffectResult(null, false, false);
    }

    protected void sitePlayedCallback(PhysicalCard site) {

    }
}
