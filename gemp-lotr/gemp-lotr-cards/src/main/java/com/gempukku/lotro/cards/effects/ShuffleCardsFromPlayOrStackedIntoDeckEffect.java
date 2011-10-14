package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ShuffleCardsFromPlayOrStackedIntoDeckEffect extends AbstractEffect {
    private PhysicalCard _source;
    private String _playerDeck;
    private Collection<PhysicalCard> _cards;

    public ShuffleCardsFromPlayOrStackedIntoDeckEffect(PhysicalCard source, String playerDeck, Collection<PhysicalCard> cards) {
        _source = source;
        _playerDeck = playerDeck;
        _cards = cards;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        for (PhysicalCard card : _cards) {
            if (card.getZone() != Zone.STACKED && !card.getZone().isInPlay())
                return false;
        }

        return true;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(LotroGame game) {
        Set<PhysicalCard> stacked = new HashSet<PhysicalCard>();
        Set<PhysicalCard> inPlay = new HashSet<PhysicalCard>();
        for (PhysicalCard card : _cards) {
            if (card.getZone() == Zone.STACKED)
                stacked.add(card);
            else if (card.getZone().isInPlay())
                inPlay.add(card);
        }

        for (PhysicalCard physicalCard : inPlay)
            game.getGameState().stopAffecting(physicalCard);

        Set<PhysicalCard> shuffleIn = new HashSet<PhysicalCard>(stacked);
        shuffleIn.addAll(inPlay);

        game.getGameState().removeCardsFromZone(shuffleIn);

        game.getGameState().shuffleCardsIntoDeck(shuffleIn, _playerDeck);

        game.getGameState().sendMessage(getAppendedNames(shuffleIn) + " is/are shuffled into " + _playerDeck + " deck");

        return new FullEffectResult(null, shuffleIn.size() == _cards.size(), shuffleIn.size() == _cards.size());
    }
}
