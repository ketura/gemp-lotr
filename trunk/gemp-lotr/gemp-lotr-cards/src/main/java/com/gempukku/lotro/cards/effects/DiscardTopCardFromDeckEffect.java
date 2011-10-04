package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class DiscardTopCardFromDeckEffect extends AbstractEffect {
    private String _playerId;

    public DiscardTopCardFromDeckEffect(String playerId) {
        _playerId = playerId;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getDeck(_playerId).size() > 0;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        boolean success = game.getGameState().getDeck(_playerId).size() > 0;
        if (success) {
            GameState gameState = game.getGameState();
            PhysicalCard card = gameState.removeTopDeckCard(_playerId);
            gameState.sendMessage(_playerId + " discards top card from his or her deck - " + card.getBlueprint().getName());
            gameState.addCardToZone(card, Zone.DISCARD);

            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }
}
