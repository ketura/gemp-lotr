package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

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