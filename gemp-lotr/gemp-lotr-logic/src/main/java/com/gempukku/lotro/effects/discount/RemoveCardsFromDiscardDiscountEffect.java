package com.gempukku.lotro.effects.discount;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.decisions.YesNoDecision;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.GameUtils;
import com.gempukku.lotro.effects.DiscountEffect;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveCardsFromDiscardDiscountEffect implements DiscountEffect {
    private final LotroPhysicalCard _source;
    private final String _playerId;
    private final int _count;
    private final int discount;
    private final Filterable _cardFilter;

    private boolean _paid;
    private boolean _required;

    public RemoveCardsFromDiscardDiscountEffect(LotroPhysicalCard source, String playerId, int count, int discount, Filterable cardFilter) {
        _source = source;
        _playerId = playerId;
        _count = count;
        this.discount = discount;
        _cardFilter = cardFilter;
    }

    @Override
    public int getDiscountPaidFor() {
        return _paid ? discount : 0;
    }

    @Override
    public void setMinimalRequiredDiscount(int minimalDiscount) {
        _required = (minimalDiscount > 0);
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.filter(game.getGameState().getDiscard(_playerId), game, _cardFilter).size() >= _count;
    }

    @Override
    public int getMaximumPossibleDiscount(DefaultGame game) {
        return Filters.filter(game.getGameState().getDiscard(_playerId), game, _cardFilter).size();
    }

    @Override
    public void playEffect(final DefaultGame game) {
        if (isPlayableInFull(game)) {
            if (!_required) {
                game.getUserFeedback().sendAwaitingDecision(_playerId,
                        new YesNoDecision("Do you want to remove cards from discard instead of paying twilight cost?") {
                            @Override
                            protected void yes() {
                                proceedDiscount(game);
                            }
                        });
            } else {
                proceedDiscount(game);
            }
        }
    }

    private void proceedDiscount(final DefaultGame game) {
        final Collection<LotroPhysicalCard> removableCards = Filters.filter(game.getGameState().getDiscard(_playerId), game, _cardFilter);
        game.getUserFeedback().sendAwaitingDecision(_playerId,
                new ArbitraryCardsSelectionDecision(1, "Choose cards to remove", removableCards, _count, _count) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        removeCards(game, getSelectedCardsByResponse(result));
                        _paid = true;
                    }
                });
    }

    private void removeCards(DefaultGame game, List<LotroPhysicalCard> cardsToRemove) {
        Set<LotroPhysicalCard> removedCards = new HashSet<>();
        for (LotroPhysicalCard physicalCard : cardsToRemove)
            if (physicalCard.getZone() == Zone.DISCARD)
                removedCards.add(physicalCard);

        game.getGameState().removeCardsFromZone(_playerId, removedCards);
        for (LotroPhysicalCard removedCard : removedCards)
            game.getGameState().addCardToZone(game, removedCard, Zone.REMOVED);

        game.getGameState().sendMessage(_playerId + " removed " + GameUtils.getAppendedNames(removedCards) + " from discard using " + GameUtils.getCardLink(_source));
        discountPaidCallback(removedCards.size());
    }

    @Override
    public boolean wasCarriedOut() {
        return !_required || _paid;
    }

    protected void discountPaidCallback(int paid) {  }
}
