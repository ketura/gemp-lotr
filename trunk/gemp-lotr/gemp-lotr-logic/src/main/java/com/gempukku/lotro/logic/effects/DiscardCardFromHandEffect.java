package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class DiscardCardFromHandEffect extends UnrespondableEffect {
    private PhysicalCard _card;

    public DiscardCardFromHandEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return _card.getZone() == Zone.HAND;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        gameState.removeCardFromZone(_card);
        gameState.addCardToZone(_card, Zone.DISCARD);
    }
}
