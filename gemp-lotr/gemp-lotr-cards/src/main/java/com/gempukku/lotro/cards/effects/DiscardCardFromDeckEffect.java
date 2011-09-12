package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class DiscardCardFromDeckEffect extends UnrespondableEffect {
    private String _playerId;
    private PhysicalCard _card;

    public DiscardCardFromDeckEffect(String playerId, PhysicalCard card) {
        _playerId = playerId;
        _card = card;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return game.getGameState().getDeck(_playerId).contains(_card);
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        gameState.removeCardFromZone(_card);
        gameState.addCardToZone(_card, Zone.DISCARD);
    }
}
