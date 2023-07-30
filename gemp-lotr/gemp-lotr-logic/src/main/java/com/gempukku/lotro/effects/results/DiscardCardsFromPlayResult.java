package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

public class DiscardCardsFromPlayResult extends EffectResult {
    private final LotroPhysicalCard _source;
    private final String _performingPlayer;
    private final LotroPhysicalCard _card;

    public DiscardCardsFromPlayResult(LotroPhysicalCard source, String performingPlayer, LotroPhysicalCard card) {
        super(EffectResult.Type.FOR_EACH_DISCARDED_FROM_PLAY);
        _source = source;
        _performingPlayer = performingPlayer;
        _card = card;
    }

    public String getPerformingPlayer() {
        return _performingPlayer;
    }

    public LotroPhysicalCard getDiscardedCard() {
        return _card;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }
}
