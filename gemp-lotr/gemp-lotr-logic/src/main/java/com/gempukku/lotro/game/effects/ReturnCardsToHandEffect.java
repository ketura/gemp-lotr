package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.results.DiscardCardsFromPlayResult;
import com.gempukku.lotro.game.timing.results.ReturnCardsToHandResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ReturnCardsToHandEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final Filterable _filter;

    public ReturnCardsToHandEffect(PhysicalCard source, Filterable filter) {
        _source = source;
        _filter = filter;
    }

    @Override
    public String getText(DefaultGame game) {
        Collection<PhysicalCard> cards = Filters.filterActive(game, _filter);
        return "Return " + getAppendedNames(cards) + " to hand";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return Filters.filterActive(game, _filter,
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, PhysicalCard physicalCard) {
                        return (_source == null || game.getModifiersQuerying().canBeReturnedToHand(game, physicalCard, _source));
                    }
                }).size() > 0;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        GameState gameState = game.getGameState();
        Collection<PhysicalCard> cardsToReturnToHand = Filters.filterActive(game, _filter);

        // Preparation, figure out, what's going where...
        Set<PhysicalCard> discardedFromPlay = new HashSet<>();
        Set<PhysicalCard> toGoToDiscardCards = new HashSet<>();

        DiscardUtils.cardsToChangeZones(game, cardsToReturnToHand, discardedFromPlay, toGoToDiscardCards);

        Set<PhysicalCard> cardsToRemoveFromZones = new HashSet<>(toGoToDiscardCards);
        cardsToRemoveFromZones.addAll(cardsToReturnToHand);

        // Remove from their zone
        gameState.removeCardsFromZone(_source.getOwner(), cardsToRemoveFromZones);

        // Add cards to hand
        for (PhysicalCard card : cardsToReturnToHand)
            gameState.addCardToZone(game, card, Zone.HAND);

        // Add discarded to discard
        for (PhysicalCard card : toGoToDiscardCards)
            gameState.addCardToZone(game, card, Zone.DISCARD);

        if (_source != null && cardsToReturnToHand.size() > 0)
            gameState.sendMessage(GameUtils.getCardLink(_source) + " returns " + getAppendedNames(cardsToReturnToHand) + " to hand");

        for (PhysicalCard discardedCard : discardedFromPlay)
            game.getActionsEnvironment().emitEffectResult(new DiscardCardsFromPlayResult(null, null, discardedCard));
        for (PhysicalCard cardReturned : cardsToReturnToHand)
            game.getActionsEnvironment().emitEffectResult(new ReturnCardsToHandResult(cardReturned));

        return new FullEffectResult(true);
    }
}
