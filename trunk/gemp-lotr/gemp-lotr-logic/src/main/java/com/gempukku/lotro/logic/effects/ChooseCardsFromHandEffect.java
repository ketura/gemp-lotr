package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;

public abstract class ChooseCardsFromHandEffect extends UnrespondableEffect {
    private String _playerId;
    private String _choiceText;
    private int _minimum;
    private int _maximum;
    private Filter[] _filters;

    public ChooseCardsFromHandEffect(String playerId, String choiceText, int minimum, int maximum, Filter... filters) {
        _playerId = playerId;
        _choiceText = choiceText;
        _minimum = minimum;
        _maximum = maximum;
        _filters = filters;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filters).size() >= _minimum;
    }

    @Override
    public void doPlayEffect(LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new CardsSelectionDecision(1, _choiceText, Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filters), _minimum, _maximum) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        cardsSelected(getSelectedCardsByResponse(result));
                    }
                });
    }

    protected abstract void cardsSelected(List<PhysicalCard> selectedCards);
}

