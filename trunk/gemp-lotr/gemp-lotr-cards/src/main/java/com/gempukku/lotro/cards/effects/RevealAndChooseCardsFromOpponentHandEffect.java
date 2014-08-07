package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class RevealAndChooseCardsFromOpponentHandEffect extends AbstractSubActionEffect {
    private Action _action;
    private String _playerId;
    private String _opponentId;
    private PhysicalCard _source;
    private String _text;
    private Filterable _selectionFilter;
    private int _minChosen;
    private int _maxChosen;

    protected RevealAndChooseCardsFromOpponentHandEffect(Action action, String playerId, String opponentId, PhysicalCard source, String text, Filterable selectionFilter, int minChosen, int maxChosen) {
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
    public boolean isPlayableInFull(LotroGame game) {
        return (game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), _opponentId, _playerId))
                && game.getGameState().getHand(_opponentId).size() >= _minChosen;
    }

    @Override
    public void playEffect(LotroGame game) {
        if (game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), _opponentId, _playerId)) {
            List<PhysicalCard> opponentHand = new LinkedList<PhysicalCard>(game.getGameState().getHand(_opponentId));

            final PlayOrder playOrder = game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_opponentId, false);
            // Skip hand owner (opponent)
            playOrder.getNextPlayer();

            String nextPlayer;
            while ((nextPlayer = playOrder.getNextPlayer()) != null) {
                if (nextPlayer.equals(_playerId)) {
                    Collection<PhysicalCard> selectable = Filters.filter(opponentHand, game.getGameState(), game.getModifiersQuerying(), _selectionFilter);

                    game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                            new ArbitraryCardsSelectionDecision(1, _text, opponentHand, new LinkedList<PhysicalCard>(selectable), Math.min(_minChosen, selectable.size()), Math.min(_maxChosen, selectable.size())) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                                    cardsSelected(selectedCards);
                                }
                            });
                } else if (!nextPlayer.equals(_opponentId)) {
                    game.getUserFeedback().sendAwaitingDecision(nextPlayer,
                            new ArbitraryCardsSelectionDecision(1, "Hand of " + _opponentId, opponentHand, Collections.<PhysicalCard>emptySet(), 0, 0) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                }
                            });
                }
            }
        }
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    protected abstract void cardsSelected(List<PhysicalCard> selectedCards);
}
