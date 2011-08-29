package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

public class ChooseAndDiscardCardFromHandEffect extends UnrespondableEffect {
    private CostToEffectAction _action;
    private String _playerId;
    private boolean _cost;

    public ChooseAndDiscardCardFromHandEffect(CostToEffectAction action, String playerId, boolean cost) {
        _action = action;
        _playerId = playerId;
        _cost = cost;
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return (game.getGameState().getHand(_playerId).size() > 0);
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new CardsSelectionDecision(1, "Choose card to discard", game.getGameState().getHand(_playerId), 1, 1) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        PhysicalCard card = getSelectedCardsByResponse(result).get(0);
                        if (_cost)
                            _action.addCost(new DiscardCardFromHandEffect(card));
                        else
                            _action.addEffect(new DiscardCardFromHandEffect(card));
                    }
                });
    }
}
