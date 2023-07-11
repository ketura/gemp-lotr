package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.timing.EffectResult;

public class AddBurdenResult extends EffectResult {
    private final String _performingPlayer;
    private final PhysicalCard _source;

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
