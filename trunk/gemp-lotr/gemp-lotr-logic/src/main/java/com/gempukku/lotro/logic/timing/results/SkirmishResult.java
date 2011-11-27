package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.HashSet;
import java.util.Set;

public abstract class SkirmishResult extends EffectResult {
    private Set<PhysicalCard> _winners;
    private Set<PhysicalCard> _inSkirmishlosers;
    private Set<PhysicalCard> _losers;

    public SkirmishResult(EffectResult.Type type, Set<PhysicalCard> winners, Set<PhysicalCard> losers, Set<PhysicalCard> removedFromSkirmish) {
        super(type);
        _winners = winners;
        _inSkirmishlosers = losers;
        _losers = new HashSet<PhysicalCard>(losers);
        _losers.addAll(removedFromSkirmish);
    }

    public Set<PhysicalCard> getWinners() {
        return _winners;
    }

    public Set<PhysicalCard> getInSkirmishLosers() {
        return _inSkirmishlosers;
    }

    public Set<PhysicalCard> getLosers() {
        return _losers;
    }
}
