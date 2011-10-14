package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReturnCardsToHandEffect extends AbstractEffect {
    private PhysicalCard _source;
    private Filter _filter;

    public ReturnCardsToHandEffect(PhysicalCard source, Filter filter) {
        _source = source;
        _filter = filter;
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
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter).size() > 0;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        Collection<PhysicalCard> cardsToReturnToHand = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter);

        // Preparation, figure out, what's going where...
        Set<PhysicalCard> stoppedAffecting = new HashSet<PhysicalCard>();
        Set<PhysicalCard> discardedFromPlay = new HashSet<PhysicalCard>();
        Set<PhysicalCard> removedFromZone = new HashSet<PhysicalCard>();

        for (PhysicalCard card : cardsToReturnToHand) {
            final List<PhysicalCard> attachedCards = game.getGameState().getAttachedCards(card);

            stoppedAffecting.add(card);
            stoppedAffecting.addAll(attachedCards);

            discardedFromPlay.addAll(attachedCards);

            removedFromZone.add(card);
            removedFromZone.addAll(attachedCards);
            removedFromZone.addAll(game.getGameState().getStackedCards(card));
        }

        discardedFromPlay.removeAll(cardsToReturnToHand);

        // Now do the actual things
        GameState gameState = game.getGameState();
        // Stop affecting
        for (PhysicalCard card : stoppedAffecting)
            gameState.stopAffecting(card);

        // Remove from their zone
        gameState.removeCardsFromZone(removedFromZone);

        // Add cards to hand
        for (PhysicalCard card : cardsToReturnToHand)
            gameState.addCardToZone(card, Zone.HAND);

        // Add discarded to discard
        for (PhysicalCard card : discardedFromPlay)
            gameState.addCardToZone(card, Zone.DISCARD);

        if (discardedFromPlay.size() > 0)
            return new FullEffectResult(new EffectResult[]{new DiscardCardsFromPlayResult(discardedFromPlay)}, true, true);

        return new FullEffectResult(null, true, true);
    }
}
