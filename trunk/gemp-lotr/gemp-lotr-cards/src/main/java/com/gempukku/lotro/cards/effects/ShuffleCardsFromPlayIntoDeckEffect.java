package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ShuffleCardsFromPlayIntoDeckEffect extends AbstractEffect {
    private PhysicalCard _source;
    private String _playerDeck;
    private Collection<PhysicalCard> _cards;

    public ShuffleCardsFromPlayIntoDeckEffect(PhysicalCard source, String playerDeck, Collection<PhysicalCard> cards) {
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
            if (!card.getZone().isInPlay())
                return false;
        }

        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        Set<PhysicalCard> goingToDiscard = new HashSet<PhysicalCard>();
        Set<PhysicalCard> discardedFromPlay = new HashSet<PhysicalCard>();
        Set<PhysicalCard> toShuffleIn = new HashSet<PhysicalCard>();

        for (PhysicalCard card : _cards) {
            if (card.getZone().isInPlay()) {
                toShuffleIn.add(card);

                goingToDiscard.addAll(game.getGameState().getStackedCards(card));
                goingToDiscard.addAll(game.getGameState().getAttachedCards(card));

                discardedFromPlay.addAll(game.getGameState().getAttachedCards(card));
            }
        }

        Set<PhysicalCard> removeFromPlay = new HashSet<PhysicalCard>();
        removeFromPlay.addAll(toShuffleIn);
        removeFromPlay.addAll(goingToDiscard);

        if (toShuffleIn.size() > 0) {
            game.getGameState().removeCardsFromZone(_source.getOwner(), removeFromPlay);

            game.getGameState().shuffleCardsIntoDeck(toShuffleIn, _playerDeck);

            for (PhysicalCard physicalCard : goingToDiscard)
                game.getGameState().addCardToZone(game, physicalCard, Zone.DISCARD);

            for (PhysicalCard physicalCard : discardedFromPlay)
                game.getActionsEnvironment().emitEffectResult(new DiscardCardsFromPlayResult(physicalCard));

            game.getGameState().sendMessage(getAppendedNames(toShuffleIn) + " " + GameUtils.be(toShuffleIn) + " shuffled into " + _playerDeck + " deck");

            cardsShuffledCallback(toShuffleIn);
        }

        return new FullEffectResult(toShuffleIn.size() == _cards.size(), toShuffleIn.size() == _cards.size());
    }

    protected void cardsShuffledCallback(Set<PhysicalCard> cardsShuffled) {

    }
}
