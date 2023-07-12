package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RemoveCardsFromTheGameEffect extends AbstractEffect {
    private final String _playerPerforming;
    private final PhysicalCard _source;
    private final Collection<? extends PhysicalCard> _cardsToRemove;

    public RemoveCardsFromTheGameEffect(String playerPerforming, PhysicalCard source, Collection<? extends PhysicalCard> cardsToRemove) {
        _playerPerforming = playerPerforming;
        _source = source;
        _cardsToRemove = cardsToRemove;
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
        for (PhysicalCard physicalCard : _cardsToRemove) {
            if (!physicalCard.getZone().isInPlay())
                return false;
        }

        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        Set<PhysicalCard> removedCards = new HashSet<>();
        for (PhysicalCard physicalCard : _cardsToRemove)
            if (physicalCard.getZone().isInPlay())
                removedCards.add(physicalCard);

        Set<PhysicalCard> discardedCards = new HashSet<>();

        Set<PhysicalCard> toMoveFromZoneToDiscard = new HashSet<>();

        DiscardUtils.cardsToChangeZones(game, removedCards, discardedCards, toMoveFromZoneToDiscard);

        Set<PhysicalCard> toRemoveFromZone = new HashSet<>();
        toRemoveFromZone.addAll(removedCards);
        toRemoveFromZone.addAll(toMoveFromZoneToDiscard);

        game.getGameState().removeCardsFromZone(_playerPerforming, toRemoveFromZone);
        for (PhysicalCard removedCard : removedCards)
            game.getGameState().addCardToZone(game, removedCard, Zone.REMOVED);
        for (PhysicalCard card : toMoveFromZoneToDiscard)
            game.getGameState().addCardToZone(game, card, Zone.DISCARD);

        game.getGameState().sendMessage(_playerPerforming + " removed " + GameUtils.getAppendedNames(removedCards) + " from the game using " + GameUtils.getCardLink(_source));

        return new FullEffectResult(_cardsToRemove.size() == removedCards.size());
    }
}
