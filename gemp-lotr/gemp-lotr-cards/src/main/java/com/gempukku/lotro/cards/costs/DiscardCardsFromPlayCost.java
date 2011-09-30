package com.gempukku.lotro.cards.costs;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscardCardsFromPlayCost extends AbstractPreventableCardCost {
    public DiscardCardsFromPlayCost(PhysicalCard... cards) {
        super(cards);
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return Filters.any();
    }

    @Override
    public String getText(LotroGame game) {
        Collection<PhysicalCard> cards = getCardsToBeAffected(game);
        return "Discard - " + getAppendedNames(cards);
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        Collection<PhysicalCard> cardsToDiscard = getCardsToBeAffected(game);

        boolean success = isSuccess(cardsToDiscard);

        Set<PhysicalCard> discardedCards = new HashSet<PhysicalCard>();
        for (PhysicalCard card : cardsToDiscard) {
            discardedCards.add(card);

            GameState gameState = game.getGameState();
            gameState.stopAffecting(card);
            gameState.removeCardFromZone(card);
            gameState.addCardToZone(card, Zone.DISCARD);

            List<PhysicalCard> attachedCards = gameState.getAttachedCards(card);
            for (PhysicalCard attachedCard : attachedCards) {
                discardedCards.add(attachedCard);

                gameState.stopAffecting(attachedCard);
                gameState.removeCardFromZone(attachedCard);
                gameState.addCardToZone(attachedCard, Zone.DISCARD);
            }

            List<PhysicalCard> stackedCards = gameState.getStackedCards(card);
            for (PhysicalCard stackedCard : stackedCards) {
                gameState.removeCardFromZone(stackedCard);
                gameState.addCardToZone(stackedCard, Zone.DISCARD);
            }
        }

        return new CostResolution(new EffectResult[]{new DiscardCardsFromPlayResult(discardedCards)}, success);
    }
}
