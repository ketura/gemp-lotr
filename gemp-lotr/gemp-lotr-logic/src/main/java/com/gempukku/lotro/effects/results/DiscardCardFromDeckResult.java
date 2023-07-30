package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

public class DiscardCardFromDeckResult extends EffectResult {
    private final LotroPhysicalCard _source;
    private final LotroPhysicalCard _card;
    private final boolean _forced;

    public DiscardCardFromDeckResult(LotroPhysicalCard source, LotroPhysicalCard card, boolean forced) {
        super(Type.FOR_EACH_DISCARDED_FROM_DECK);
        _source = source;
        _card = card;
        _forced = forced;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }

    public boolean isForced() {
        return _forced;
    }


    public LotroPhysicalCard getDiscardedCard() {
        return _card;
    }
}