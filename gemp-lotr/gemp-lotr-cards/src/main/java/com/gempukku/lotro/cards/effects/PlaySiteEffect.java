package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PlaySiteEffect extends UnrespondableEffect {
    private String _playerId;
    private int _siteNumber;

    public PlaySiteEffect(String playerId, int siteNumber) {
        _playerId = playerId;
        _siteNumber = siteNumber;
    }

    @Override
    public void playEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        PhysicalCard card = gameState.getSite(_siteNumber);
        if (card != null) {
            gameState.stopAffecting(card);
            gameState.removeCardFromZone(card);
            gameState.addCardToZone(card, Zone.DECK);
        }
        PhysicalCard newSite = Filters.filter(gameState.getAdventureDeck(_playerId), game.getGameState(), game.getModifiersQuerying(), Filters.siteNumber(_siteNumber)).get(0);
        gameState.addCardToZone(newSite, Zone.ADVENTURE_PATH);
        gameState.startAffecting(newSite, game.getModifiersEnvironment());
    }
}
