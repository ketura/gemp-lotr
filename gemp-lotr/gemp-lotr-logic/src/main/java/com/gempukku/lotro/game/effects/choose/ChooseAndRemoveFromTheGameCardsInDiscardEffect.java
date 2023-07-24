package com.gempukku.lotro.game.effects.choose;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.effects.RemoveCardsFromDiscardEffect;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.actions.lotronly.SubAction;
import com.gempukku.lotro.game.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.effects.AbstractSubActionEffect;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;
import java.util.List;

public class ChooseAndRemoveFromTheGameCardsInDiscardEffect extends AbstractSubActionEffect {
    private final Action _action;
    private final LotroPhysicalCard _source;
    private final String _playerId;
    private final int _minimum;
    private final int _maximum;
    private final Filterable[] _filters;
    private CostToEffectAction _resultSubAction;
    private boolean _success;

    public ChooseAndRemoveFromTheGameCardsInDiscardEffect(Action action, LotroPhysicalCard source, String playerId, int minimum, int maximum, Filterable... filters) {
        _action = action;
        _source = source;
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _filters = filters;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.filter(game.getGameState().getDiscard(_playerId), game, _filters).size() >= _minimum;
    }

    @Override
    public void playEffect(final DefaultGame game) {
        final Collection<LotroPhysicalCard> possibleTargets = Filters.filter(game.getGameState().getDiscard(_playerId), game, _filters);

        if (possibleTargets.size() <= _minimum) {
            processForCards(game, possibleTargets);
        } else {
            int min = _minimum;
            int max = Math.min(_maximum, possibleTargets.size());
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new ArbitraryCardsSelectionDecision(1, "Choose cards to remove from the game", possibleTargets, min, max) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            final List<LotroPhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            processForCards(game, selectedCards);
                        }
                    });
        }
    }

    private void processForCards(DefaultGame game, Collection<LotroPhysicalCard> cards) {
        _resultSubAction = new SubAction(_action);
        _resultSubAction.appendEffect(
                new RemoveCardsFromDiscardEffect(_playerId, _source, cards));
        processSubAction(game, _resultSubAction);
        _success = cards.size() >= _minimum;
    }

    @Override
    public boolean wasCarriedOut() {
        return super.wasCarriedOut() && _success;
    }
}
