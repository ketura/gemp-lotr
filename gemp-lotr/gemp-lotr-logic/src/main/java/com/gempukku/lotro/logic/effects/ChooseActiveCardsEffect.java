package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.ChooseableEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Set;

public abstract class ChooseActiveCardsEffect implements ChooseableEffect {
    private String _playerId;
    private String _choiceText;
    private int _minimum;
    private int _maximum;
    private Filter[] _filters;

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
    public boolean canPlayEffect(LotroGame game) {
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
    public EffectResult[] playEffect(LotroGame game) {
        Collection<PhysicalCard> matchingCards = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.and(_filters, getExtraFilter()));

        if (matchingCards.size() < _minimum)
            _minimum = matchingCards.size();

        if (matchingCards.size() == _minimum) {
            cardsSelected(matchingCards);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, _choiceText, matchingCards, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                            cardsSelected(selectedCards);
                        }
                    });
        }

        return null;
    }

    protected abstract void cardsSelected(Collection<PhysicalCard> cards);
}
