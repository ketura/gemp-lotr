package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.effects.results.DiscardCardFromDeckResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DiscardTopCardFromDeckEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final String _playerId;
    private final int _count;
    private final boolean _forced;

    public DiscardTopCardFromDeckEffect(LotroPhysicalCard source, String playerId, boolean forced) {
        this(source, playerId, 1, forced);
    }

    public DiscardTopCardFromDeckEffect(LotroPhysicalCard source, String playerId, int count, boolean forced) {
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
            List<LotroPhysicalCard> cardsDiscarded = new LinkedList<>();
            for (int i = 0; i < _count; i++) {
                LotroPhysicalCard card = gameState.removeTopDeckCard(_playerId);
                if (card != null) {
                    cardsDiscarded.add(card);
                    gameState.addCardToZone(game, card, Zone.DISCARD);
                }
            }
            if (cardsDiscarded.size() > 0) {
                gameState.sendMessage(_playerId + " discards top cards from their deck - " + getAppendedNames(cardsDiscarded));
                cardsDiscardedCallback(cardsDiscarded);
            }

            for (LotroPhysicalCard discardedCard : cardsDiscarded)//PhysicalCard source, PhysicalCard card, String handPlayerId, boolean forced
                game.getActionsEnvironment().emitEffectResult(new DiscardCardFromDeckResult(_source, discardedCard, _forced));

            return new FullEffectResult(_count == cardsDiscarded.size());
        }
        return new FullEffectResult(false);
    }

    protected void cardsDiscardedCallback(Collection<LotroPhysicalCard> cards) {

    }
}
