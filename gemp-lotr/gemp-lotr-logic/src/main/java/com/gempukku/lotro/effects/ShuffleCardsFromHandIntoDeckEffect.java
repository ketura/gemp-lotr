package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ShuffleCardsFromHandIntoDeckEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final String _playerDeck;
    private final Collection<? extends LotroPhysicalCard> _cards;

    public ShuffleCardsFromHandIntoDeckEffect(LotroPhysicalCard source, String playerDeck, Collection<? extends LotroPhysicalCard> cards) {
        _source = source;
        _playerDeck = playerDeck;
        _cards = cards;
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
        for (LotroPhysicalCard card : _cards) {
            if (card.getZone() != Zone.HAND)
                return false;
        }

        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        Set<LotroPhysicalCard> toShuffleIn = new HashSet<>();
        for (LotroPhysicalCard card : _cards) {
            if (card.getZone() == Zone.HAND)
                toShuffleIn.add(card);
        }

        if (toShuffleIn.size() > 0) {
            game.getGameState().removeCardsFromZone(_source.getOwner(), toShuffleIn);

            game.getGameState().shuffleCardsIntoDeck(toShuffleIn, _playerDeck);

            game.getGameState().sendMessage(getAppendedNames(toShuffleIn) + " shuffled cards from hand into " + _playerDeck + "'s deck");
        }

        return new FullEffectResult(toShuffleIn.size() == _cards.size());
    }
}
