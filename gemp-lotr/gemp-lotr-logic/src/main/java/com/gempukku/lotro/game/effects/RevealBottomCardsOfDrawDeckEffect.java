package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.decisions.ArbitraryCardsSelectionDecision;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class RevealBottomCardsOfDrawDeckEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final String _playerId;
    private final int _count;

    public RevealBottomCardsOfDrawDeckEffect(PhysicalCard source, String playerId, int count) {
        _source = source;
        _playerId = playerId;
        _count = count;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getGameState().getDeck(_playerId).size() >= _count;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        List<? extends PhysicalCard> deck = game.getGameState().getDeck(_playerId);
        int count = Math.min(deck.size(), _count);
        LinkedList<PhysicalCard> bottomCards = new LinkedList<>(deck.subList(deck.size() - count, deck.size()));

        if (bottomCards.size() > 0) {
            final PlayOrder playerOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_source.getOwner(), false);

            String nextPlayer;
            while ((nextPlayer = playerOrder.getNextPlayer()) != null) {
                game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                        new ArbitraryCardsSelectionDecision(1, _playerId+" revealed card(s) from bottom of deck", bottomCards, Collections.emptySet(), 0, 0) {
                            @Override
                            public void decisionMade(String result) {
                            }
                        });
            }

            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " revealed cards from bottom of " + _playerId + " deck - " + getAppendedNames(bottomCards));
        }
        cardsRevealed(bottomCards);
        return new FullEffectResult(bottomCards.size() == _count);
    }

    protected abstract void cardsRevealed(List<PhysicalCard> cards);
}
