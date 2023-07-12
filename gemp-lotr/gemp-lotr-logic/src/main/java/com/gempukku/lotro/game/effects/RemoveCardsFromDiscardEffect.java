package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RemoveCardsFromDiscardEffect extends AbstractEffect {
    private final String _playerPerforming;
    private final PhysicalCard _source;
    private final Collection<? extends PhysicalCard> _cardsToRemove;

    public RemoveCardsFromDiscardEffect(String playerPerforming, PhysicalCard source, Collection<? extends PhysicalCard> cardsToRemove) {
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
            if (physicalCard.getZone() != Zone.DISCARD)
                return false;
        }

        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        Set<PhysicalCard> removedCards = new HashSet<>();
        for (PhysicalCard physicalCard : _cardsToRemove)
            if (physicalCard.getZone() == Zone.DISCARD)
                removedCards.add(physicalCard);

        game.getGameState().removeCardsFromZone(_playerPerforming, removedCards);
        for (PhysicalCard removedCard : removedCards)
            game.getGameState().addCardToZone(game, removedCard, Zone.REMOVED);

        game.getGameState().sendMessage(_playerPerforming + " removed " + GameUtils.getAppendedNames(removedCards) + " from discard using " + GameUtils.getCardLink(_source));

        return new FullEffectResult(_cardsToRemove.size() == removedCards.size());
    }
}
