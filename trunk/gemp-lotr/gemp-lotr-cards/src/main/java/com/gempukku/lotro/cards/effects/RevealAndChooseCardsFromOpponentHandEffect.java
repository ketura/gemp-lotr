package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class RevealAndChooseCardsFromOpponentHandEffect extends AbstractEffect {
    private Action _action;
    private String _playerId;
    private String _opponentId;
    private PhysicalCard _source;
    private String _text;
    private Filter _selectionFilter;
    private int _minChosen;
    private int _maxChosen;

    protected RevealAndChooseCardsFromOpponentHandEffect(Action action, String playerId, String opponentId, PhysicalCard source, String text, Filter selectionFilter, int minChosen, int maxChosen) {
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
        return (game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), _opponentId))
                && game.getGameState().getHand(_opponentId).size() >= _minChosen;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        if (game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), _opponentId)) {
            List<PhysicalCard> opponentHand = new LinkedList<PhysicalCard>(game.getGameState().getHand(_opponentId));

            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(
                    new RevealCardsFromHandEffect(_source, _opponentId, opponentHand));
            game.getActionsEnvironment().addActionToStack(subAction);

            Collection<PhysicalCard> selectable = Filters.filter(opponentHand, game.getGameState(), game.getModifiersQuerying(), _selectionFilter);

            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, _text, opponentHand, new LinkedList<PhysicalCard>(selectable), Math.min(_minChosen, selectable.size()), Math.min(_maxChosen, selectable.size())) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            cardsSelected(selectedCards);
                        }
                    });
            return new FullEffectResult(null, true, true);
        }
        return new FullEffectResult(null, false, false);
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
