package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Set;

public class ChooseAndDiscardCardsFromHandEffect extends AbstractEffect {
    private Action _action;
    private String _playerId;
    private boolean _forced;
    private int _minimum;
    private int _maximum;
    private Filter _filter;

    public ChooseAndDiscardCardsFromHandEffect(Action action, String playerId, boolean forced, int minimum, int maximum, Filter filter) {
        _action = action;
        _playerId = playerId;
        _forced = forced;
        _minimum = minimum;
        _maximum = maximum;
        _filter = filter;
    }

    public ChooseAndDiscardCardsFromHandEffect(Action action, String playerId, boolean forced, int count, Filter filter) {
        this(action, playerId, forced, count, count, filter);
    }

    public ChooseAndDiscardCardsFromHandEffect(Action action, String playerId, boolean forced, int count) {
        this(action, playerId, forced, count, Filters.any);
    }

    public ChooseAndDiscardCardsFromHandEffect(Action action, String playerId, boolean forced) {
        this(action, playerId, forced, 1);
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard card(s) from hand";
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter).size() >= _minimum;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        if (_forced && !game.getModifiersQuerying().canDiscardCardsFromHand(game.getGameState(), _playerId, _action.getActionSource()))
            return new FullEffectResult(null, false, false);

        Collection<PhysicalCard> hand = Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter);

        final boolean success = hand.size() >= _minimum;

        if (hand.size() <= _minimum) {
            SubAction subAction = new SubAction(_action);
            subAction.appendEffect(new DiscardCardsFromHandEffect(_action.getActionSource(), _playerId, hand, _forced));
            game.getActionsEnvironment().addActionToStack(subAction);
            cardsBeingDiscarded(hand, success);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose card(s) to discard", hand, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> cards = getSelectedCardsByResponse(result);
                            SubAction subAction = new SubAction(_action);
                            subAction.appendEffect(new DiscardCardsFromHandEffect(_action.getActionSource(), _playerId, cards, _forced));
                            game.getActionsEnvironment().addActionToStack(subAction);
                            cardsBeingDiscarded(cards, success);
                        }
                    });
        }

        return new FullEffectResult(null, success, success);
    }

    protected void cardsBeingDiscarded(Collection<PhysicalCard> cardsBeingDiscarded, boolean success) {
    }
}
