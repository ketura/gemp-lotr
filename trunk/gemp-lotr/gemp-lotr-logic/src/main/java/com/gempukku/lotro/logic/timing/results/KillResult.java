package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Set;

public class KillResult extends EffectResult {
    private Set<PhysicalCard> _killedCards;

    public KillResult(Set<PhysicalCard> killedCards) {
        super(EffectResult.Type.KILL);
        _killedCards = killedCards;
    }

    public Set<PhysicalCard> getKilledCards() {
        return _killedCards;
    }
}
