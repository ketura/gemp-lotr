package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Set;

public class ChooseAndStackCardsFromHandEffect extends AbstractEffect {
    private Action _action;
    private String _playerId;
    private int _minimum;
    private int _maximum;
    private PhysicalCard _stackOn;
    private Filter _filter;

    public ChooseAndStackCardsFromHandEffect(Action action, String playerId, int minimum, int maximum, PhysicalCard stackOn, Filter filter) {
        _action = action;
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _stackOn = stackOn;
        _filter = filter;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Stack card(s) from hand";
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter).size() >= _minimum;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        Collection<PhysicalCard> hand = Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter);

        final boolean success = hand.size() >= _minimum;

        if (hand.size() <= _minimum) {
            SubAction subAction = new SubAction(_action);
            for (PhysicalCard card : hand)
                subAction.appendEffect(new StackCardFromHandEffect(_action.getActionSource(), card));
            game.getActionsEnvironment().addActionToStack(subAction);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose card(s) to stack", hand, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> cards = getSelectedCardsByResponse(result);
                            SubAction subAction = new SubAction(_action);
                            for (PhysicalCard card : cards)
                                subAction.appendEffect(new StackCardFromHandEffect(_action.getActionSource(), card));
                            game.getActionsEnvironment().addActionToStack(subAction);
                        }
                    });
        }

        return new FullEffectResult(null, success, success);
    }
}