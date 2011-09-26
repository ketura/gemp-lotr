package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Set;

public class DiscardCardsFromPlayResult extends EffectResult {
    private Set<PhysicalCard> _cards;

    public DiscardCardsFromPlayResult(Set<PhysicalCard> cards) {
        super(EffectResult.Type.DISCARD_FROM_PLAY);
        _cards = cards;
    }

    public Set<PhysicalCard> getDiscardedCards() {
        return _cards;
    }
}
