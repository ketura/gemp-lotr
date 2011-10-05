package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

public class PlaySiteEffect extends UnrespondableEffect {
    private String _playerId;
    private int _siteNumber;

    public PlaySiteEffect(String playerId, Block siteBlock, int siteNumber) {
        _playerId = playerId;
        _siteNumber = siteNumber;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        PhysicalCard newSite = Filters.filter(gameState.getAdventureDeck(_playerId), game.getGameState(), game.getModifiersQuerying(), Filters.siteNumber(_siteNumber)).iterator().next();
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
                gameState.removeCardFromZone(card);
                gameState.addCardToZone(card, Zone.DECK);
            }

            gameState.sendMessage(newSite.getOwner() + " plays " + GameUtils.getCardLink(newSite));
            gameState.addCardToZone(newSite, Zone.ADVENTURE_PATH);
            if (gameState.getCurrentSiteNumber() == _siteNumber)
                gameState.startAffecting(newSite, game.getModifiersEnvironment());

            if (controlled != null) {
                gameState.takeControlOfCard(controlled, newSite, zone);
                for (PhysicalCard physicalCard : stacked)
                    gameState.stackCard(physicalCard, newSite);
            }
        }
    }
}
