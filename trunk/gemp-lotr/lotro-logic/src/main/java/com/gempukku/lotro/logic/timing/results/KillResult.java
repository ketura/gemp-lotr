package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class KillResult extends EffectResult {
    private PhysicalCard _killedCard;

    public KillResult(PhysicalCard killedCard) {
        super(EffectResult.Type.KILL);
        _killedCard = killedCard;
    }

    public PhysicalCard getKilledCard() {
        return _killedCard;
    }
}
