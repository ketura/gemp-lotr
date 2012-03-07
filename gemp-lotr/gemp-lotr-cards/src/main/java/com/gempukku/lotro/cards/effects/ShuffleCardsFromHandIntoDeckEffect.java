package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ShuffleCardsFromHandIntoDeckEffect extends AbstractEffect {
    private PhysicalCard _source;
    private String _playerDeck;
    private Collection<PhysicalCard> _cards;

    public ShuffleCardsFromHandIntoDeckEffect(PhysicalCard source, String playerDeck, Collection<PhysicalCard> cards) {
        _source = source;
        _playerDeck = playerDeck;
        _cards = cards;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        for (PhysicalCard card : _cards) {
            if (card.getZone() != Zone.HAND)
                return false;
        }

        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        Set<PhysicalCard> toShuffleIn = new HashSet<PhysicalCard>();
        for (PhysicalCard card : _cards) {
            if (card.getZone() == Zone.HAND)
                toShuffleIn.add(card);
        }

        if (toShuffleIn.size() > 0) {
            game.getGameState().removeCardsFromZone(_source.getOwner(), toShuffleIn);

            game.getGameState().shuffleCardsIntoDeck(toShuffleIn, _playerDeck);

            game.getGameState().sendMessage(getAppendedNames(toShuffleIn) + " shuffled cards from hand into " + _playerDeck + " deck");
        }

        return new FullEffectResult(toShuffleIn.size() == _cards.size(), toShuffleIn.size() == _cards.size());
    }
}
