package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class RevealAndChooseCardsFromOpponentHandEffect extends UnrespondableEffect {
    private String _playerId;
    private String _opponentId;
    private String _text;
    private Filter _selectionFilter;
    private int _minChosen;
    private int _maxChosen;

    protected RevealAndChooseCardsFromOpponentHandEffect(String playerId, String opponentId, String text, Filter selectionFilter, int minChosen, int maxChosen) {
        _playerId = playerId;
        _opponentId = opponentId;
        _text = text;
        _selectionFilter = selectionFilter;
        _minChosen = minChosen;
        _maxChosen = maxChosen;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        if (game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), _opponentId)) {
            List<PhysicalCard> opponentHand = new LinkedList<PhysicalCard>(game.getGameState().getHand(_opponentId));
            Collection<PhysicalCard> selectable = Filters.filter(opponentHand, game.getGameState(), game.getModifiersQuerying(), _selectionFilter);

            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, _text, opponentHand, new LinkedList<PhysicalCard>(selectable), Math.min(_minChosen, selectable.size()), Math.min(_maxChosen, selectable.size())) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            cardsSelected(selectedCards);
                        }
                    });
        }
    }

    protected abstract void cardsSelected(List<PhysicalCard> selectedCards);
}
