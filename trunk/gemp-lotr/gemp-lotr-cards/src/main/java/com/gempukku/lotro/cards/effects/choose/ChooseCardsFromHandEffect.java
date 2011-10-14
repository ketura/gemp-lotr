package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Set;

public abstract class ChooseCardsFromHandEffect extends AbstractEffect {
    private final String _playerId;
    private final int _minimum;
    private final int _maximum;
    private final Filter _filter;

    public ChooseCardsFromHandEffect(String playerId, int minimum, int maximum, Filter... filters) {
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _filter = Filters.and(filters);
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter).size() >= _minimum;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        final Collection<PhysicalCard> selectableCards = Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter);

        boolean success = selectableCards.size() >= _minimum;

        int minimum = Math.min(_minimum, selectableCards.size());

        if (minimum == selectableCards.size())
            cardsSelected(game, selectableCards);
        else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose card(s) from hand", selectableCards, minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            cardsSelected(game, selectedCards);
                        }
                    }
            );
        }

        return new FullEffectResult(null, success, success);
    }

    protected abstract void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards);
}
