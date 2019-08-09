package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collection;
import java.util.List;

public class DiscardUtils {
    public static void cardsToChangeZones(LotroGame game, Collection<PhysicalCard> movingCards, Collection<PhysicalCard> discardedCards, Collection<PhysicalCard> toMoveToDiscard) {
        for (PhysicalCard card : movingCards) {
            cardsToChangeZones(game, movingCards, card, discardedCards, toMoveToDiscard);
        }
    }

    private static void cardsToChangeZones(LotroGame game, Collection<PhysicalCard> movingCards, PhysicalCard card, Collection<PhysicalCard> discardedCards, Collection<PhysicalCard> toMoveToDiscard) {
        List<PhysicalCard> attachedCards = game.getGameState().getAttachedCards(card);
        for (PhysicalCard attachedCard : attachedCards) {
            if (!movingCards.contains(attachedCard)) {
                discardedCards.add(attachedCard);
                toMoveToDiscard.add(attachedCard);
                cardsToChangeZones(game, movingCards, attachedCard, discardedCards, toMoveToDiscard);
            }
        }

        List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(card);
        for (PhysicalCard stackedCard : stackedCards)
            toMoveToDiscard.add(stackedCard);
    }
}
