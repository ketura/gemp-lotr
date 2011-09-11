package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.List;

public class ChooseAndDiscardCardsFromHandEffect extends UnrespondableEffect {
    private CostToEffectAction _action;
    private String _playerId;
    private int _count;
    private boolean _cost;

    public ChooseAndDiscardCardsFromHandEffect(CostToEffectAction action, String playerId, boolean cost, int count) {
        _action = action;
        _playerId = playerId;
        _cost = cost;
        _count = count;
    }

    public ChooseAndDiscardCardsFromHandEffect(CostToEffectAction action, String playerId, boolean cost) {
        this(action, playerId, cost, 1);
    }

    @Override
    public String getText() {
        return "Discard " + _count + " card(s) from hand";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return (game.getGameState().getHand(_playerId).size() >= _count);
    }

    @Override
    public void playEffect(LotroGame game) {
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new CardsSelectionDecision(1, "Choose card(s) to discard", game.getGameState().getHand(_playerId), _count, _count) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        List<PhysicalCard> cards = getSelectedCardsByResponse(result);
                        for (PhysicalCard card : cards) {
                            if (_cost)
                                _action.addCost(new DiscardCardFromHandEffect(card));
                            else
                                _action.addEffect(new DiscardCardFromHandEffect(card));
                        }
                    }
                });
    }
}
