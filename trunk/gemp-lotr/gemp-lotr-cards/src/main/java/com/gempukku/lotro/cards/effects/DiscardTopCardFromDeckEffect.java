package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class DiscardTopCardFromDeckEffect extends UnrespondableEffect {
    private String _playerId;

    public DiscardTopCardFromDeckEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return game.getGameState().getDeck(_playerId).size() > 0;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        GameState gameState = game.getGameState();
        PhysicalCard card = gameState.removeTopDeckCard(_playerId);
        gameState.sendMessage(_playerId + " discards top card from his or her deck - " + card.getBlueprint().getName());
        gameState.addCardToZone(card, Zone.DISCARD);
    }
}
