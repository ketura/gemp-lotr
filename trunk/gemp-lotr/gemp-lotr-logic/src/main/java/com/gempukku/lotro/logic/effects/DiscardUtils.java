package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;

import java.util.Collection;
import java.util.List;

public class DiscardUtils {
    public static void cardsToChangeZones(GameState gameState, Collection<PhysicalCard> movingCards, Collection<PhysicalCard> discardedCards, Collection<PhysicalCard> toMoveToDiscard) {
        for (PhysicalCard card : movingCards) {
            cardsToChangeZones(gameState, movingCards, card, discardedCards, toMoveToDiscard);
        }
    }

    private static void cardsToChangeZones(GameState gameState, Collection<PhysicalCard> movingCards, PhysicalCard card, Collection<PhysicalCard> discardedCards, Collection<PhysicalCard> toMoveToDiscard) {
        List<PhysicalCard> attachedCards = gameState.getAttachedCards(card);
        for (PhysicalCard attachedCard : attachedCards) {
            if (!movingCards.contains(attachedCard)) {
                discardedCards.add(attachedCard);
                toMoveToDiscard.add(attachedCard);
                cardsToChangeZones(gameState, movingCards, attachedCard, discardedCards, toMoveToDiscard);
            }
        }

        List<PhysicalCard> stackedCards = gameState.getStackedCards(card);
        for (PhysicalCard stackedCard : stackedCards)
            toMoveToDiscard.add(stackedCard);
    }
}
