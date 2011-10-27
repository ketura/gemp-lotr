package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.StackCardFromHandEffect;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.List;

public class ChooseAndStackCardsFromDiscardEffect extends AbstractEffect {
    private Action _action;
    private String _playerId;
    private int _minimum;
    private int _maximum;
    private PhysicalCard _stackOn;
    private Filter _filter;

    public ChooseAndStackCardsFromDiscardEffect(Action action, String playerId, int minimum, int maximum, PhysicalCard stackOn, Filter filter) {
        _action = action;
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _stackOn = stackOn;
        _filter = filter;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.filter(game.getGameState().getDiscard(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter).size() >= _minimum;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        Collection<PhysicalCard> discard = Filters.filter(game.getGameState().getDiscard(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter);

        final boolean success = discard.size() >= _minimum;

        if (discard.size() <= _minimum) {
            SubAction subAction = new SubAction(_action);
            for (PhysicalCard card : discard)
                subAction.appendEffect(new StackCardFromHandEffect(card, _stackOn));
            game.getActionsEnvironment().addActionToStack(subAction);
            stackFromDiscardCallback(discard);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Choose cards to stack", discard, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            List<PhysicalCard> cards = getSelectedCardsByResponse(result);
                            SubAction subAction = new SubAction(_action);
                            for (PhysicalCard card : cards)
                                subAction.appendEffect(new StackCardFromDiscardEffect(card, _stackOn));
                            game.getActionsEnvironment().addActionToStack(subAction);
                            stackFromDiscardCallback(cards);
                        }
                    });
        }

        return new FullEffectResult(null, success, success);
    }

    public void stackFromDiscardCallback(Collection<PhysicalCard> cardsStacked) {

    }
}