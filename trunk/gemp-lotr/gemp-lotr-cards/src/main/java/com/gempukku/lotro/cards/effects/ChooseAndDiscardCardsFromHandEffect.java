package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.cards.costs.FailCost;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.timing.ChooseableCost;
import com.gempukku.lotro.logic.timing.ChooseableEffect;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ChooseAndDiscardCardsFromHandEffect implements ChooseableEffect, ChooseableCost {
    private CostToEffectAction _action;
    private String _playerId;
    private int _minimum;
    private int _maximum;
    private Filter _filter;

    public ChooseAndDiscardCardsFromHandEffect(CostToEffectAction action, String playerId, int minimum, int maximum, Filter filter) {
        _action = action;
        _playerId = playerId;
        _minimum = minimum;
        _maximum = maximum;
        _filter = filter;
    }

    public ChooseAndDiscardCardsFromHandEffect(CostToEffectAction action, String playerId, int count, Filter filter) {
        this(action, playerId, count, count, filter);
    }

    public ChooseAndDiscardCardsFromHandEffect(CostToEffectAction action, String playerId, int count) {
        this(action, playerId, count, Filters.any());
    }

    public ChooseAndDiscardCardsFromHandEffect(CostToEffectAction action, String playerId) {
        this(action, playerId, 1);
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public String getText(LotroGame game) {
        return "Discard card(s) from hand";
    }

    @Override
    public boolean canPlayEffect(LotroGame game) {
        return canPlayCost(game);
    }

    @Override
    public boolean canPlayCost(LotroGame game) {
        return Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter).size() >= _minimum;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        Collection<PhysicalCard> hand = Filters.filter(game.getGameState().getHand(_playerId), game.getGameState(), game.getModifiersQuerying(), _filter);

        boolean success = hand.size() >= _minimum;

        if (hand.size() <= _minimum) {
            for (PhysicalCard card : hand) {
                _action.appendCost(new DiscardCardFromHandEffect(card));
            }
            cardsBeingDiscarded(hand);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose card(s) to discard", hand, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> cards = getSelectedCardsByResponse(result);
                            for (PhysicalCard card : cards) {
                                _action.appendCost(new DiscardCardFromHandEffect(card));
                            }
                            cardsBeingDiscarded(cards);
                        }
                    });
        }

        if (!success)
            _action.appendCost(new FailCost());

        return new CostResolution(null, true);
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        Set<PhysicalCard> hand = new HashSet<PhysicalCard>(game.getGameState().getHand(_playerId));

        if (hand.size() <= _minimum) {
            for (PhysicalCard card : hand) {
                _action.insertEffect(new DiscardCardFromHandEffect(card));
            }
            cardsBeingDiscarded(hand);
        } else {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose card(s) to discard", hand, _minimum, _maximum) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            Set<PhysicalCard> cards = getSelectedCardsByResponse(result);
                            for (PhysicalCard card : cards) {
                                _action.insertEffect(new DiscardCardFromHandEffect(card));
                            }
                            cardsBeingDiscarded(cards);
                        }
                    });
        }

        return null;
    }

    protected void cardsBeingDiscarded(Collection<PhysicalCard> cardsBeingDiscarded) {
    }
}
