package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

public class ForEachKilledResult extends EffectResult {
    private PhysicalCard _killedCard;
    private KillEffect.Cause _cause;

    public ForEachKilledResult(PhysicalCard killedCard, KillEffect.Cause cause) {
        super(EffectResult.Type.FOR_EACH_KILLED);
        _killedCard = killedCard;
        _cause = cause;
    }

    public PhysicalCard getKilledCard() {
        return _killedCard;
    }

    public KillEffect.Cause getCause() {
        return _cause;
    }
}
