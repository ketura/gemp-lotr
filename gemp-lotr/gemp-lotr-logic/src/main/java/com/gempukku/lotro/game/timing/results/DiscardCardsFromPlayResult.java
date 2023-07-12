package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class DiscardCardsFromPlayResult extends EffectResult {
    private final PhysicalCard _source;
    private final String _performingPlayer;
    private final PhysicalCard _card;

    public DiscardCardsFromPlayResult(PhysicalCard source, String performingPlayer, PhysicalCard card) {
        super(EffectResult.Type.FOR_EACH_DISCARDED_FROM_PLAY);
        _source = source;
        _performingPlayer = performingPlayer;
        _card = card;
    }

    public String getPerformingPlayer() {
        return _performingPlayer;
    }

    public PhysicalCard getDiscardedCard() {
        return _card;
    }

    public PhysicalCard getSource() {
        return _source;
    }
}
