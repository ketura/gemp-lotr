package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class DiscardCardsFromHandResult extends EffectResult {
    private PhysicalCard _source;
    private Collection<PhysicalCard> _cards;

    public DiscardCardsFromHandResult(PhysicalCard source, Collection<PhysicalCard> cards) {
        super(Type.DISCARD_FROM_HAND);
        _source = source;
        _cards = cards;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public Collection<PhysicalCard> getDiscardedCards() {
        return _cards;
    }
}