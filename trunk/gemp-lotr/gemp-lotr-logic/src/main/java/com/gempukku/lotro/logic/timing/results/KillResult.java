package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Set;

public class KillResult extends EffectResult {
    private Set<PhysicalCard> _killedCards;
    private KillEffect.Cause _cause;

    public KillResult(Set<PhysicalCard> killedCards, KillEffect.Cause cause) {
        super(EffectResult.Type.KILL);
        _killedCards = killedCards;
        _cause = cause;
    }

    public Set<PhysicalCard> getKilledCards() {
        return _killedCards;
    }

    public KillEffect.Cause getCause() {
        return _cause;
    }
}
