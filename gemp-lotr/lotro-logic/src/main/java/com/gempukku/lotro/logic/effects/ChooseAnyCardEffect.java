package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public abstract class ChooseAnyCardEffect extends UnrespondableEffect {
    private String _playerId;
    private String _choiceText;
    private Filter[] _filters;

    public ChooseAnyCardEffect(String playerId, String choiceText, Filter... filters) {
        _playerId = playerId;
        _choiceText = choiceText;
        _filters = filters;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return Filters.canSpotAnywhere(game.getGameState(), game.getModifiersQuerying(), _filters);
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new CardsSelectionDecision(1, _choiceText, Filters.filterAnywhere(game.getGameState(), game.getModifiersQuerying(), _filters), 1, 1) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        PhysicalCard selectedCard = getSelectedCardsByResponse(result).get(0);
                        cardSelected(selectedCard);
                    }
                });
    }

    protected abstract void cardSelected(PhysicalCard card);
}
