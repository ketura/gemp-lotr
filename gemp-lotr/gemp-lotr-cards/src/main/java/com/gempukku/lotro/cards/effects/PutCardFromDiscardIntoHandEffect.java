package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class PutCardFromDiscardIntoHandEffect extends UnrespondableEffect {
    private PhysicalCard _card;

    public PutCardFromDiscardIntoHandEffect(PhysicalCard card) {
        _card = card;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        if (game.getModifiersQuerying().canDrawCardAndIncrement(game.getGameState(), _card.getOwner())) {
            GameState gameState = game.getGameState();
            gameState.sendMessage(_card.getOwner() + " puts " + _card.getBlueprint().getName() + " from discard into his or her hand");
            gameState.removeCardFromZone(_card);
            gameState.addCardToZone(_card, Zone.HAND);
        }
    }
}
