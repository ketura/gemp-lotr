package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class RevealAndChooseCardsFromOpponentHandEffect extends AbstractSubActionEffect {
    private final Action _action;
    private final String _playerId;
    private final String _opponentId;
    private final LotroPhysicalCard _source;
    private final String _text;
    private final Filterable _selectionFilter;
    private final int _minChosen;
    private final int _maxChosen;

    protected RevealAndChooseCardsFromOpponentHandEffect(Action action, String playerId, String opponentId, LotroPhysicalCard source, String text, Filterable selectionFilter, int minChosen, int maxChosen) {
        _action = action;
        _playerId = playerId;
        _opponentId = opponentId;
        _source = source;
        _text = text;
        _selectionFilter = selectionFilter;
        _minChosen = minChosen;
        _maxChosen = maxChosen;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return (game.getModifiersQuerying().canLookOrRevealCardsInHand(game, _opponentId, _playerId))
                && game.getGameState().getHand(_opponentId).size() >= _minChosen;
    }

    @Override
    public void playEffect(DefaultGame game) {
        if (game.getModifiersQuerying().canLookOrRevealCardsInHand(game, _opponentId, _playerId)) {
            List<LotroPhysicalCard> opponentHand = new LinkedList<>(game.getGameState().getHand(_opponentId));
            game.getGameState().sendMessage(GameUtils.getCardLink(_source) + " revealed " + _opponentId + " cards in hand - " + getAppendedNames(opponentHand));

            final PlayOrder playOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_opponentId, false);
            // Skip hand owner (opponent)
            playOrder.getNextPlayer();

            String nextPlayer;
            while ((nextPlayer = playOrder.getNextPlayer()) != null) {
                if (nextPlayer.equals(_playerId)) {
                    Collection<LotroPhysicalCard> selectable = Filters.filter(opponentHand, game, _selectionFilter);

                    game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                            new ArbitraryCardsSelectionDecision(1, _text, opponentHand, new LinkedList<>(selectable), Math.min(_minChosen, selectable.size()), Math.min(_maxChosen, selectable.size())) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    List<LotroPhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                                    cardsSelected(selectedCards);
                                }
                            });
                } else if (!nextPlayer.equals(_opponentId)) {
                    game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                            new ArbitraryCardsSelectionDecision(1, "Hand of " + _opponentId, opponentHand, Collections.emptySet(), 0, 0) {
                                @Override
                                public void decisionMade(String result) {
                                }
                            });
                }
            }
        }
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    protected abstract void cardsSelected(List<LotroPhysicalCard> selectedCards);
}
