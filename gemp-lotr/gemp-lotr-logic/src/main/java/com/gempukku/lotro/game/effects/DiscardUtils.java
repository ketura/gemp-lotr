package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

import java.util.Collection;
import java.util.List;

public class DiscardUtils {
    public static void cardsToChangeZones(DefaultGame game, Collection<LotroPhysicalCard> movingCards, Collection<LotroPhysicalCard> discardedCards, Collection<LotroPhysicalCard> toMoveToDiscard) {
        for (LotroPhysicalCard card : movingCards) {
            cardsToChangeZones(game, movingCards, card, discardedCards, toMoveToDiscard);
        }
    }

    private static void cardsToChangeZones(DefaultGame game, Collection<LotroPhysicalCard> movingCards, LotroPhysicalCard card, Collection<LotroPhysicalCard> discardedCards, Collection<LotroPhysicalCard> toMoveToDiscard) {
        List<LotroPhysicalCard> attachedCards = game.getGameState().getAttachedCards(card);
        for (LotroPhysicalCard attachedCard : attachedCards) {
            if (!movingCards.contains(attachedCard)) {
                discardedCards.add(attachedCard);
                toMoveToDiscard.add(attachedCard);
                cardsToChangeZones(game, movingCards, attachedCard, discardedCards, toMoveToDiscard);
            }
        }

        List<LotroPhysicalCard> stackedCards = game.getGameState().getStackedCards(card);
        toMoveToDiscard.addAll(stackedCards);
    }
}
