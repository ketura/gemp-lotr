package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DiscardTopCardFromDeckEffect extends AbstractEffect {
    private PhysicalCard _source;
    private String _playerId;
    private int _count;
    private boolean _forced;

    public DiscardTopCardFromDeckEffect(PhysicalCard source, String playerId, boolean forced) {
        this(source, playerId, 1, forced);
    }

    public DiscardTopCardFromDeckEffect(PhysicalCard source, String playerId, int count, boolean forced) {
        _source = source;
        _playerId = playerId;
        _count = count;
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
        return game.getGameState().getDeck(_playerId).size() >= _count
                && (!_forced || game.getModifiersQuerying().canDiscardCardsFromTopOfDeck(game.getGameState(), _playerId, _source));
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (!_forced || game.getModifiersQuerying().canDiscardCardsFromTopOfDeck(game.getGameState(), _playerId, _source)) {
            GameState gameState = game.getGameState();
            List<PhysicalCard> cardsDiscarded = new LinkedList<PhysicalCard>();
            for (int i = 0; i < _count; i++) {
                PhysicalCard card = gameState.removeTopDeckCard(_playerId);
                if (card != null) {
                    cardsDiscarded.add(card);
                    gameState.addCardToZone(game, card, Zone.DISCARD);
                }
            }
            if (cardsDiscarded.size() > 0) {
                gameState.sendMessage(_playerId + " discards top cards from his or her deck - " + getAppendedNames(cardsDiscarded));
                cardsDiscardedCallback(cardsDiscarded);
            }

            return new FullEffectResult(null, _count == cardsDiscarded.size(), _count == cardsDiscarded.size());
        }
        return new FullEffectResult(null, false, false);
    }

    protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {

    }
}
