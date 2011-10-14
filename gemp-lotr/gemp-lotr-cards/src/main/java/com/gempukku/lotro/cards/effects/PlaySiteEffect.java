package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PlaySiteEffect extends AbstractEffect {
    private String _playerId;
    private Block _siteBlock;
    private int _siteNumber;

    public PlaySiteEffect(String playerId, Block siteBlock, int siteNumber) {
        _playerId = playerId;
        _siteBlock = siteBlock;
        _siteNumber = siteNumber;
    }

    private PhysicalCard getMatchingSite(LotroGame game) {
        Collection<PhysicalCard> matching;
        if (_siteBlock != null) {
            matching = Filters.filter(game.getGameState().getAdventureDeck(_playerId), game.getGameState(), game.getModifiersQuerying(), Filters.siteNumber(_siteNumber), Filters.siteBlock(_siteBlock));
        } else {
            matching = Filters.filter(game.getGameState().getAdventureDeck(_playerId), game.getGameState(), game.getModifiersQuerying(), Filters.siteNumber(_siteNumber));
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
    public EffectResult.Type getType() {
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

                if (gameState.getCurrentSiteNumber() == _siteNumber)
                    gameState.stopAffecting(card);
                gameState.removeCardsFromZone(Collections.singleton(card));
                gameState.addCardToZone(card, Zone.DECK);
            }

            gameState.sendMessage(newSite.getOwner() + " plays " + GameUtils.getCardLink(newSite));
            gameState.addCardToZone(newSite, Zone.ADVENTURE_PATH);
            if (gameState.getCurrentSiteNumber() == _siteNumber)
                gameState.startAffecting(game, newSite, game.getModifiersEnvironment());

            if (controlled != null) {
                gameState.takeControlOfCard(controlled, newSite, zone);
                for (PhysicalCard physicalCard : stacked)
                    gameState.stackCard(physicalCard, newSite);
            }

            sitePlayedCallback(newSite);
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }

    protected void sitePlayedCallback(PhysicalCard site) {

    }
}
