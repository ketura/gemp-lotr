package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class DiscardBottomCardFromDeckEffect extends UnrespondableEffect implements Cost {
    private String _playerId;

    public DiscardBottomCardFromDeckEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        boolean success = game.getGameState().getDeck(_playerId).size() > 0;
        if (success) {
            GameState gameState = game.getGameState();
            PhysicalCard card = gameState.removeBottomDeckCard(_playerId);
            gameState.sendMessage(_playerId + " discards bottom card from his or her deck - " + card.getBlueprint().getName());
            gameState.addCardToZone(card, Zone.DISCARD);

            discardedCard(card);

            return new CostResolution(null, true);
        }
        return new CostResolution(null, false);
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        if (game.getGameState().getDeck(_playerId).size() > 0) {
            GameState gameState = game.getGameState();
            PhysicalCard card = gameState.removeBottomDeckCard(_playerId);
            gameState.sendMessage(_playerId + " discards bottom card from his or her deck - " + card.getBlueprint().getName());
            gameState.addCardToZone(card, Zone.DISCARD);

            discardedCard(card);
        }
    }

    protected void discardedCard(PhysicalCard card) {

    }
}
