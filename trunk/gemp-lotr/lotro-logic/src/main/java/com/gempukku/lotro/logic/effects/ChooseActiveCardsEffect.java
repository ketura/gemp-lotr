package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;

public abstract class ChooseActiveCardsEffect extends UnrespondableEffect {
    private String _playerId;
    private String _choiceText;
    private int _maximum;
    private Filter[] _filters;

    public ChooseActiveCardsEffect(String playerId, String choiceText, int maximum, Filter... filters) {
        _playerId = playerId;
        _choiceText = choiceText;
        _maximum = maximum;
        _filters = filters;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), _filters);
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new CardsSelectionDecision(1, _choiceText, Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filters), 1, _maximum) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                        cardsSelected(selectedCards);
                    }
                });
    }

    protected abstract void cardsSelected(List<PhysicalCard> cards);
}
