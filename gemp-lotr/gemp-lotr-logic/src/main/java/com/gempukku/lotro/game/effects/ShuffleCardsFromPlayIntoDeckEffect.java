package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.timing.results.DiscardCardsFromPlayResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ShuffleCardsFromPlayIntoDeckEffect extends AbstractEffect {
    private final PhysicalCard _source;
    private final String _playerDeck;
    private final Collection<? extends PhysicalCard> _cards;

    public ShuffleCardsFromPlayIntoDeckEffect(PhysicalCard source, String playerDeck, Collection<? extends PhysicalCard> cards) {
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
        for (PhysicalCard card : _cards) {
            if (!card.getZone().isInPlay())
                return false;
        }

        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(DefaultGame game) {
        Set<PhysicalCard> goingToDiscard = new HashSet<>();
        Set<PhysicalCard> discardedFromPlay = new HashSet<>();
        Set<PhysicalCard> toShuffleIn = new HashSet<>();

        for (PhysicalCard card : _cards) {
            if (card.getZone().isInPlay()) {
                toShuffleIn.add(card);
            }
        }

        if (toShuffleIn.size() > 0) {
            DiscardUtils.cardsToChangeZones(game, toShuffleIn, discardedFromPlay, goingToDiscard);

            Set<PhysicalCard> removeFromPlay = new HashSet<>(goingToDiscard);
            removeFromPlay.addAll(toShuffleIn);

            game.getGameState().removeCardsFromZone(_source.getOwner(), removeFromPlay);

            game.getGameState().shuffleCardsIntoDeck(toShuffleIn, _playerDeck);

            for (PhysicalCard physicalCard : goingToDiscard)
                game.getGameState().addCardToZone(game, physicalCard, Zone.DISCARD);

            for (PhysicalCard physicalCard : discardedFromPlay)
                game.getActionsEnvironment().emitEffectResult(new DiscardCardsFromPlayResult(null, null, physicalCard));

            game.getGameState().sendMessage(getAppendedNames(toShuffleIn) + " " + GameUtils.be(toShuffleIn) + " shuffled into " + _playerDeck + " deck");

            cardsShuffledCallback(toShuffleIn);
        }

        return new FullEffectResult(toShuffleIn.size() == _cards.size());
    }

    protected void cardsShuffledCallback(Set<PhysicalCard> cardsShuffled) {

    }
}
