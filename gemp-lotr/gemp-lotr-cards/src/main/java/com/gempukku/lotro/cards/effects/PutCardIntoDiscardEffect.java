package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PutCardIntoDiscardEffect extends UnrespondableEffect {
    private PhysicalCard _card;

    public PutCardIntoDiscardEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        gameState.addCardToZone(_card, Zone.DISCARD);
    }
}
