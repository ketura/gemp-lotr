package com.gempukku.lotro.game.effects.choose;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.game.effects.AbstractEffect;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;
import java.util.List;

public class ChooseAndStackCardsFromDiscardEffect extends AbstractEffect {
    private final Action _action;
    private final String _playerId;
    private final int _minimum;
    private final int _maximum;
    private final LotroPhysicalCard _stackOn;
    private final Filterable[] _filter;

    public ChooseAndStackCardsFromDiscardEffect(Action action, String playerId, int minimum, int maximum, LotroPhysicalCard stackOn, Filterable... filter) {
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
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.filter(game.getGameState().getDiscard(_playerId), game, _filter).size() >= _minimum;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final DefaultGame game) {
        Collection<LotroPhysicalCard> discard = Filters.filter(game.getGameState().getDiscard(_playerId), game, _filter);

        final boolean success = discard.size() >= _minimum;

        if (discard.size() <= _minimum) {
            SubAction subAction = new SubAction(_action);
            for (LotroPhysicalCard card : discard)
                subAction.appendEffect(new StackCardFromDiscardEffect(card, _stackOn));
            game.getActionsEnvironment().addActionToStack(subAction);
            stackFromDiscardCallback(discard);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Choose cards to stack", discard, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            List<LotroPhysicalCard> cards = getSelectedCardsByResponse(result);
                            SubAction subAction = new SubAction(_action);
                            for (LotroPhysicalCard card : cards)
                                subAction.appendEffect(new StackCardFromDiscardEffect(card, _stackOn));
                            game.getActionsEnvironment().addActionToStack(subAction);
                            stackFromDiscardCallback(cards);
                        }
                    });
        }

        return new FullEffectResult(success);
    }

    public void stackFromDiscardCallback(Collection<LotroPhysicalCard> cardsStacked) {

    }
}