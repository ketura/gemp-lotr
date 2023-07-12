package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.timing.results.DiscardCardFromDeckResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DiscardTopCardFromDeckEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final String _playerId;
    private final int _count;
    private final boolean _forced;

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
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().getDeck(_playerId).size() >= _count
                && (!_forced || game.getModifiersQuerying().canDiscardCardsFromTopOfDeck(game, _playerId, _source));
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (!_forced || game.getModifiersQuerying().canDiscardCardsFromTopOfDeck(game, _playerId, _source)) {
            GameState gameState = game.getGameState();
            List<PhysicalCard> cardsDiscarded = new LinkedList<>();
            for (int i = 0; i < _count; i++) {
                PhysicalCard card = gameState.removeTopDeckCard(_playerId);
                if (card != null) {
                    cardsDiscarded.add(card);
                    gameState.addCardToZone(game, card, Zone.DISCARD);
                }
            }
            if (cardsDiscarded.size() > 0) {
                gameState.sendMessage(_playerId + " discards top cards from their deck - " + getAppendedNames(cardsDiscarded));
                cardsDiscardedCallback(cardsDiscarded);
            }

            for (PhysicalCard discardedCard : cardsDiscarded)//PhysicalCard source, PhysicalCard card, String handPlayerId, boolean forced
                game.getActionsEnvironment().emitEffectResult(new DiscardCardFromDeckResult(_source, discardedCard, _forced));

            return new FullEffectResult(_count == cardsDiscarded.size());
        }
        return new FullEffectResult(false);
    }

    protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {

    }
}
