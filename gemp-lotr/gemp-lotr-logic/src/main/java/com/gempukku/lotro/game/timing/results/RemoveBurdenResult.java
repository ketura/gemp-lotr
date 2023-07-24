package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

public class RemoveBurdenResult extends EffectResult {
    private final String _performingPlayerId;
    private final LotroPhysicalCard _source;

    public RemoveBurdenResult(String performingPlayerId, LotroPhysicalCard source) {
        super(EffectResult.Type.REMOVE_BURDEN);
        _performingPlayerId = performingPlayerId;
        _source = source;
    }

    public String getPerformingPlayerId() {
        return _performingPlayerId;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }
}
