package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.DiscardUtils;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;
import com.gempukku.lotro.logic.timing.results.ReturnCardsToHandResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ReturnCardsToHandEffect extends AbstractEffect {
    private PhysicalCard _source;
    private Filterable _filter;

    public ReturnCardsToHandEffect(PhysicalCard source, Filterable filter) {
        _source = source;
        _filter = filter;
    }

    @Override
    public String getText(LotroGame game) {
        Collection<PhysicalCard> cards = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter);
        return "Return " + getAppendedNames(cards) + " to hand";
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), _filter,
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return (_source == null || modifiersQuerying.canBeReturnedToHand(gameState, physicalCard, _source));
                    }
                }).size() > 0;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        GameState gameState = game.getGameState();
        Collection<PhysicalCard> cardsToReturnToHand = Filters.filterActive(gameState, game.getModifiersQuerying(), _filter);

        // Preparation, figure out, what's going where...
        Set<PhysicalCard> discardedFromPlay = new HashSet<PhysicalCard>();
        Set<PhysicalCard> toGoToDiscardCards = new HashSet<PhysicalCard>();

        DiscardUtils.cardsToChangeZones(gameState, cardsToReturnToHand, discardedFromPlay, toGoToDiscardCards);

        Set<PhysicalCard> cardsToRemoveFromZones = new HashSet<PhysicalCard>(toGoToDiscardCards);
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
            game.getActionsEnvironment().emitEffectResult(new DiscardCardsFromPlayResult(null, discardedCard));
        for (PhysicalCard cardReturned : cardsToReturnToHand)
            game.getActionsEnvironment().emitEffectResult(new ReturnCardsToHandResult(cardReturned));

        return new FullEffectResult(true);
    }
}
