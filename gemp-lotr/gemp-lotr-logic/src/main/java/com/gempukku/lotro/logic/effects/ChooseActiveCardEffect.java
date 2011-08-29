package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public abstract class ChooseActiveCardEffect extends UnrespondableEffect {
    private String _playerId;
    private String _choiceText;
    private Filter[] _filters;

    public ChooseActiveCardEffect(String playerId, String choiceText, Filter... filters) {
        _playerId = playerId;
        _choiceText = choiceText;
        _filters = filters;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), _filters);
    }

    @Override
    public void playEffect(final LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new CardsSelectionDecision(1, _choiceText, Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filters), 1, 1) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        PhysicalCard selectedCard = getSelectedCardsByResponse(result).get(0);
                        cardSelected(game, selectedCard);
                    }
                });
    }

    protected abstract void cardSelected(LotroGame game, PhysicalCard card);
}
