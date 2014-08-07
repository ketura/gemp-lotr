package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.results.RevealCardFromHandResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RevealHandEffect extends AbstractEffect {
    private PhysicalCard _source;
    private String _actingPlayer;
    private String _handPlayerId;

    public RevealHandEffect(PhysicalCard source, String actingPlayer, String handPlayerId) {
        _source = source;
        _actingPlayer = actingPlayer;
        _handPlayerId = handPlayerId;
    }

    @Override
    public String getText(LotroGame game) {
        return "Reveal cards from hand";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), _handPlayerId, _actingPlayer);
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        final List<? extends PhysicalCard> hand = game.getGameState().getHand(_handPlayerId);
        game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " revealed " + _handPlayerId + " cards in hand - " + getAppendedNames(hand));

        final PlayOrder playerOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_handPlayerId, false);
        // Skip hand owner
        playerOrder.getNextPlayer();

        String nextPlayer;
        while ((nextPlayer = playerOrder.getNextPlayer()) != null) {
            game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                    new ArbitraryCardsSelectionDecision(1, "Hand of " + _handPlayerId, hand, Collections.<PhysicalCard>emptySet(), 0, 0) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                        }
                    });
        }

        cardsRevealed(hand);

        for (PhysicalCard card : hand) {
            game.getActionsEnvironment().emitEffectResult(new RevealCardFromHandResult(_source, _handPlayerId, card));
        }

        return new FullEffectResult(true);
    }

    protected void cardsRevealed(Collection<? extends PhysicalCard> cards) {

    }
}
