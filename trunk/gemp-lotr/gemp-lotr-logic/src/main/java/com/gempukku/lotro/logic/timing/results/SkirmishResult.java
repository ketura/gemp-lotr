package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Set;

public abstract class SkirmishResult extends EffectResult {
    private Set<PhysicalCard> _winners;
    private Set<PhysicalCard> _inSkirmishlosers;

    public SkirmishResult(EffectResult.Type type, Set<PhysicalCard> winners, Set<PhysicalCard> losers) {
        super(type);
        _winners = winners;
        _inSkirmishlosers = losers;
    }

    public Set<PhysicalCard> getWinners() {
        return _winners;
    }

    public Set<PhysicalCard> getInSkirmishLosers() {
        return _inSkirmishlosers;
    }
}
