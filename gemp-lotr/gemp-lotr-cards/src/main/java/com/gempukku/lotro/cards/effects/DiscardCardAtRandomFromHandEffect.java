package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;
import java.util.Random;

public class DiscardCardAtRandomFromHandEffect extends UnrespondableEffect {
    private String _playerId;

    public DiscardCardAtRandomFromHandEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        List<? extends PhysicalCard> hand = gameState.getHand(_playerId);
        if (hand.size() > 0) {
            PhysicalCard randomCard = hand.get(new Random().nextInt(hand.size()));
            gameState.sendMessage(_playerId + " randomly discards " + randomCard.getBlueprint().getName());
            gameState.removeCardFromZone(randomCard);
            gameState.addCardToZone(randomCard, Zone.DISCARD);
        } else {
            setFailed();
        }
    }
}
