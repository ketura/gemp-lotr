package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class AddBurdenResult extends EffectResult {
    private String _performingPlayer;
    private PhysicalCard _source;

    public AddBurdenResult(String performingPlayer, PhysicalCard source) {
        super(EffectResult.Type.ADD_BURDEN);
        _performingPlayer = performingPlayer;
        _source = source;
    }

    public PhysicalCard getSource() {
        return _source;
    }

    public String getPerformingPlayer() {
        return _performingPlayer;
    }
}
