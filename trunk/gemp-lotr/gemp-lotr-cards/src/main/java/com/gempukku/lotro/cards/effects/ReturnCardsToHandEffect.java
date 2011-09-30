package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.*;

public class ReturnCardsToHandEffect implements Effect {
    private PhysicalCard _source;
    private Filter _filter;

    public ReturnCardsToHandEffect(PhysicalCard source, PhysicalCard... cards) {
        _source = source;
        List<PhysicalCard> affectedCards = Arrays.asList(cards);
        _filter = Filters.in(affectedCards);
    }

    @Override
    public String getText(LotroGame game) {
        return "Return card(s) to hand";
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        Collection<PhysicalCard> cardsToReturnToHand = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter);

        Set<PhysicalCard> discardedCards = new HashSet<PhysicalCard>();
        for (PhysicalCard card : cardsToReturnToHand) {

            GameState gameState = game.getGameState();
            gameState.stopAffecting(card);
            gameState.removeCardFromZone(card);
            gameState.addCardToZone(card, Zone.HAND);

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

        if (discardedCards.size() > 0)
            return new EffectResult[]{new DiscardCardsFromPlayResult(discardedCards)};

        return null;
    }
}
