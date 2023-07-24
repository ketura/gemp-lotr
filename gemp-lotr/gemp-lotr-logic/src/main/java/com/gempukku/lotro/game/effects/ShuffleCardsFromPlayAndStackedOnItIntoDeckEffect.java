package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ShuffleCardsFromPlayAndStackedOnItIntoDeckEffect extends AbstractEffect {
    private final LotroPhysicalCard _source;
    private final String _playerDeck;
    private final Collection<LotroPhysicalCard> _cards;

    public ShuffleCardsFromPlayAndStackedOnItIntoDeckEffect(LotroPhysicalCard source, String playerDeck, Collection<LotroPhysicalCard> cards) {
        _source = source;
        _playerDeck = playerDeck;
        _cards = cards;
    }

    @Override
    public String getText(DefaultGame game) {
        return null;
    }

    @Override
    public Effect.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        for (LotroPhysicalCard card : _cards) {
            if (card.getZone() != Zone.STACKED && !card.getZone().isInPlay())
                return false;
        }

        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        Set<LotroPhysicalCard> toShuffleIn = new HashSet<>();
        Set<LotroPhysicalCard> inPlay = new HashSet<>();

        for (LotroPhysicalCard card : _cards) {
            if (card.getZone().isInPlay()) {
                inPlay.add(card);
                toShuffleIn.add(card);
                toShuffleIn.addAll(game.getGameState().getStackedCards(card));
            }
        }

        if (toShuffleIn.size() > 0) {
            game.getGameState().removeCardsFromZone(_source.getOwner(), toShuffleIn);

            game.getGameState().shuffleCardsIntoDeck(toShuffleIn, _playerDeck);

            game.getGameState().sendMessage(getAppendedNames(toShuffleIn) + " " + GameUtils.be(toShuffleIn) + " shuffled into " + _playerDeck + " deck");

            cardsShuffledCallback(toShuffleIn);
        }

        return new FullEffectResult(inPlay.size() == _cards.size());
    }

    protected void cardsShuffledCallback(Set<LotroPhysicalCard> cardsShuffled) {

    }
}
