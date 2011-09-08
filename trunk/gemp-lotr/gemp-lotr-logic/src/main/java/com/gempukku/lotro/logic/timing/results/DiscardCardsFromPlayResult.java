package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class DiscardCardsFromPlayResult extends EffectResult {
    private List<PhysicalCard> _cards;

    public DiscardCardsFromPlayResult(List<PhysicalCard> cards) {
        super(EffectResult.Type.DISCARD_FROM_PLAY);
        _cards = cards;
    }

    public List<PhysicalCard> getDiscardedCards() {
        return _cards;
    }
}
