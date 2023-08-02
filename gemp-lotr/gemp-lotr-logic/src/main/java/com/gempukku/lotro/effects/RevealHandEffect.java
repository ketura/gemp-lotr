package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.game.PlayOrder;
import com.gempukku.lotro.effects.results.RevealCardFromHandResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RevealHandEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final String _actingPlayer;
    private final String _handPlayerId;

    public RevealHandEffect(LotroPhysicalCard source, String actingPlayer, String handPlayerId) {
        _source = source;
        _actingPlayer = actingPlayer;
        _handPlayerId = handPlayerId;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Reveal cards from hand";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return game.getModifiersQuerying().canLookOrRevealCardsInHand(game, _handPlayerId, _actingPlayer);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        if (game.getModifiersQuerying().canLookOrRevealCardsInHand(game, _handPlayerId, _actingPlayer)) {
            final List<? extends LotroPhysicalCard> hand = game.getGameState().getHand(_handPlayerId);
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " revealed " + _handPlayerId + " cards in hand - " + getAppendedNames(hand));

            final PlayOrder playerOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_handPlayerId, false);
            // Skip hand owner
            playerOrder.getNextPlayer();

            String nextPlayer;
            while ((nextPlayer = playerOrder.getNextPlayer()) != null) {
                game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                        new ArbitraryCardsSelectionDecision(1, "Hand of " + _handPlayerId, hand, Collections.emptySet(), 0, 0) {
                            @Override
                            public void decisionMade(String result) {
                            }
                        });
            }

            cardsRevealed(hand);

            for (LotroPhysicalCard card : hand) {
                game.getActionsEnvironment().emitEffectResult(new RevealCardFromHandResult(_source, _handPlayerId, card));
            }

            return new FullEffectResult(true);
        } else {
            return new FullEffectResult(false);
        }
    }

    protected void cardsRevealed(Collection<? extends LotroPhysicalCard> cards) {

    }
}
