package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class DiscardCardFromDeckEffect extends UnrespondableEffect {
    private PhysicalCard _card;

    public DiscardCardFromDeckEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        if (game.getGameState().getDeck(_card.getOwner()).contains(_card)) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(GameUtils.getCardLink(_card) + " gets discarded from deck");
            gameState.removeCardFromZone(_card);
            gameState.addCardToZone(_card, Zone.DISCARD);
        }
    }
}
