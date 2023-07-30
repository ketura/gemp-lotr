package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

public class AddBurdenResult extends EffectResult {
    private final String _performingPlayer;
    private final LotroPhysicalCard _source;

    public AddBurdenResult(String performingPlayer, LotroPhysicalCard source) {
        super(EffectResult.Type.ADD_BURDEN);
        _performingPlayer = performingPlayer;
        _source = source;
    }

    public LotroPhysicalCard getSource() {
        return _source;
    }

    public String getPerformingPlayer() {
        return _performingPlayer;
    }
}
