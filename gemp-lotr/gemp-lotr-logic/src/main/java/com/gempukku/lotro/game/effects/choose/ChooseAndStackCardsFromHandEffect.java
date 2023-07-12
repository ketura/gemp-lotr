package com.gempukku.lotro.game.effects.choose;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.effects.StackCardFromHandEffect;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.decisions.CardsSelectionDecision;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.effects.AbstractEffect;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.effects.Effect;

import java.util.Collection;
import java.util.Set;

public class ChooseAndStackCardsFromHandEffect extends AbstractEffect {
    private final Action _action;
    private final String _playerId;
    private final int _minimum;
    private final int _maximum;
    private final PhysicalCard _stackOn;
    private final Filterable[] _filters;

    public ChooseAndStackCardsFromHandEffect(Action action, String playerId, int minimum, int maximum, PhysicalCard stackOn, Filterable... filters) {
        _action = action;
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _stackOn = stackOn;
        _filters = filters;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Stack card from hand";
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.filter(game.getGameState().getHand(_playerId), game, _filters).size() >= _minimum;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final DefaultGame game) {
        Collection<PhysicalCard> hand = Filters.filter(game.getGameState().getHand(_playerId), game, _filters);
        int maximum = Math.min(_maximum, hand.size());

        final boolean success = hand.size() >= _minimum;

        if (hand.size() <= _minimum) {
            SubAction subAction = new SubAction(_action);
            for (PhysicalCard card : hand)
                subAction.appendEffect(new StackCardFromHandEffect(card, _stackOn));
            game.getActionsEnvironment().addActionToStack(subAction);
            stackFromHandCallback(hand);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose cards to stack", hand, _minimum, maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> cards = getSelectedCardsByResponse(result);
                            SubAction subAction = new SubAction(_action);
                            for (PhysicalCard card : cards)
                                subAction.appendEffect(new StackCardFromHandEffect(card, _stackOn));
                            game.getActionsEnvironment().addActionToStack(subAction);
                            stackFromHandCallback(cards);
                        }
                    });
        }

        return new FullEffectResult(success);
    }

    public void stackFromHandCallback(Collection<PhysicalCard> cardsStacked) {

    }
}