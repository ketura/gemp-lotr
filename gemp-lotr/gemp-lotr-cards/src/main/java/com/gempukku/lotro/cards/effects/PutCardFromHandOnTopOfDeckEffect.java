package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PutCardFromHandOnTopOfDeckEffect extends UnrespondableEffect {
    private PhysicalCard _physicalCard;

    public PutCardFromHandOnTopOfDeckEffect(PhysicalCard physicalCard) {
        _physicalCard = physicalCard;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        gameState.sendMessage(_physicalCard.getOwner() + " puts a card from hand on top of his or her deck");
        gameState.removeCardFromZone(_physicalCard);
        gameState.putCardOnTopOfDeck(_physicalCard);
    }
}