package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

public class DiscardTopCardFromDeckEffect extends AbstractEffect {
    private PhysicalCard _source;
    private String _playerId;
    private boolean _forced;

    public DiscardTopCardFromDeckEffect(PhysicalCard source, String playerId, boolean forced) {
        _source = source;
        _playerId = playerId;
        _forced = forced;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getGameState().getDeck(_playerId).size() > 0
                && (!_forced || game.getModifiersQuerying().canDiscardCardsFromTopOfDeck(game.getGameState(), _playerId, _source));
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (isPlayableInFull(game)) {
            GameState gameState = game.getGameState();
            PhysicalCard card = gameState.removeTopDeckCard(_playerId);
            gameState.sendMessage(_playerId + " discards top card from his or her deck - " + GameUtils.getCardLink(card));
            gameState.addCardToZone(game, card, Zone.DISCARD);
            cardDiscardedCallback(card);

            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
    }

    protected void cardDiscardedCallback(PhysicalCard card) {

    }
}
