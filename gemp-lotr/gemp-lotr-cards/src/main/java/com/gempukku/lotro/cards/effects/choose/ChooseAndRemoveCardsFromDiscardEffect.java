package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.effects.RemoveCardsFromDiscardEffect;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractSubActionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.List;

public class ChooseAndRemoveCardsFromDiscardEffect extends AbstractSubActionEffect {
    private Action _action;
    private PhysicalCard _source;
    private String _playerId;
    private int _minimum;
    private int _maximum;
    private Filterable[] _filters;

    public ChooseAndRemoveCardsFromDiscardEffect(Action action, PhysicalCard source, String playerId, int minimum, int maximum, Filterable... filters) {
        _action = action;
        _source = source;
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _filters = filters;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.filter(game.getGameState().getDiscard(_playerId), game.getGameState(), game.getModifiersQuerying(), _filters).size() >= _minimum;
    }

    @Override
    public void playEffect(LotroGame game) {
        final Collection<PhysicalCard> possibleTargets = Filters.filter(game.getGameState().getDiscard(_playerId), game.getGameState(), game.getModifiersQuerying(), _filters);

        if (possibleTargets.size() <= _minimum) {
            processForCards(possibleTargets);
        } else {
            int min = _minimum;
            int max = Math.min(_maximum, possibleTargets.size());
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Choose cards to remove from play", possibleTargets, min, max) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            final List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            processForCards(selectedCards);
                        }
                    });
        }
    }

    private void processForCards(Collection<PhysicalCard> cards) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(
                new RemoveCardsFromDiscardEffect(_playerId, _source, cards));
        if (cards.size() < _minimum) {
            // It has failed...
            subAction.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                        }

                        @Override
                        public boolean wasCarriedOut() {
                            return false;
                        }

                        @Override
                        public boolean wasSuccessful() {
                            return false;
                        }
                    });
        }
    }
}
