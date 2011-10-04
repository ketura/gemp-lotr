package com.gempukku.lotro.logic.effects;

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

public abstract class ChooseActiveCardsEffect extends AbstractEffect {
    private final String _playerId;
    private final String _choiceText;
    private final int _minimum;
    private final int _maximum;
    private final Filter[] _filters;

    public ChooseActiveCardsEffect(String playerId, String choiceText, int minimum, int maximum, Filter... filters) {
        _playerId = playerId;
        _choiceText = choiceText;
        _minimum = minimum;
        _maximum = maximum;
        _filters = filters;
    }

    protected Filter getExtraFilter() {
        return Filters.any();
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.and(_filters, getExtraFilter())) >= _minimum;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        final Collection<PhysicalCard> matchingCards = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.and(_filters, getExtraFilter()));

        int minimum = _minimum;
        if (matchingCards.size() < minimum)
            minimum = matchingCards.size();

        if (matchingCards.size() == minimum) {
            cardsSelected(game, matchingCards);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, _choiceText, matchingCards, minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            cardsSelected(game, selectedCards);
                        }
                    });
        }

        return new FullEffectResult(null, matchingCards.size() >= _minimum, matchingCards.size() >= _minimum);
    }

    protected abstract void cardsSelected(LotroGame game, Collection<PhysicalCard> cards);
}
