package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class DiscardCardFromHandResult extends EffectResult {
    private final LotroPhysicalCard _source;
    private final LotroPhysicalCard _card;
    private final String _handPlayerId;
    private final boolean _forced;

    public DiscardCardFromHandResult(LotroPhysicalCard source, LotroPhysicalCard card, String handPlayerId, boolean forced) {
        super(Type.FOR_EACH_DISCARDED_FROM_HAND);
        _source = source;
        _card = card;
        _handPlayerId = handPlayerId;
        _forced = forced;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }

    public boolean isForced() {
        return _forced;
    }

    public String getHandPlayerId() {
        return _handPlayerId;
    }

    public LotroPhysicalCard getDiscardedCard() {
        return _card;
    }
}