package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;

public class DiscardCardsFromHandResult extends EffectResult {
    private PhysicalCard _source;
    private Collection<PhysicalCard> _cards;
    private boolean _forced;

    public DiscardCardsFromHandResult(PhysicalCard source, Collection<PhysicalCard> cards, boolean forced) {
        super(Type.DISCARD_FROM_HAND);
        _source = source;
        _cards = cards;
        _forced = forced;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public Collection<PhysicalCard> getDiscardedCards() {
        return _cards;
    }

    public boolean isForced() {
        return _forced;
    }
}