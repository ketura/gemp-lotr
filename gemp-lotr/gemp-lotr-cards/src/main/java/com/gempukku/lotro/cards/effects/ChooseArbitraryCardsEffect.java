package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;

public abstract class ChooseArbitraryCardsEffect extends UnrespondableEffect {
    private String _playerId;
    private String _choiceText;
    private List<? extends PhysicalCard> _cards;
    private Filter _filter;
    private int _minimum;
    private int _maximum;

    public ChooseArbitraryCardsEffect(String playerId, String choiceText, List<? extends PhysicalCard> cards, int minimum, int maximum) {
        this(playerId, choiceText, cards, Filters.any(), minimum, maximum);
    }

    public ChooseArbitraryCardsEffect(String playerId, String choiceText, List<? extends PhysicalCard> cards, Filter filter, int minimum, int maximum) {
        _playerId = playerId;
        _choiceText = choiceText;
        _cards = cards;
        _filter = filter;
        _minimum = minimum;
        _maximum = maximum;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return (Filters.filter(_cards, game.getGameState(), game.getModifiersQuerying(), _filter).size() >= _minimum);
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new ArbitraryCardsSelectionDecision(1, _choiceText, Filters.filter(_cards, game.getGameState(), game.getModifiersQuerying(), _filter), _minimum, _maximum) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        cardsSelected(getSelectedCardsByResponse(result));
                    }
                });
    }

    protected abstract void cardsSelected(List<PhysicalCard> selectedCards);
}
