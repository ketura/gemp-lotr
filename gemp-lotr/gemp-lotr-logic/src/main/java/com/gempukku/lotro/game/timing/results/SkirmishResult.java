package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.timing.EffectResult;

import java.util.HashSet;
import java.util.Set;

public abstract class SkirmishResult extends EffectResult {
    private final Set<PhysicalCard> _winners;
    private final Set<PhysicalCard> _inSkirmishlosers;
    private final Set<PhysicalCard> _losers;

    public SkirmishResult(EffectResult.Type type, Set<PhysicalCard> winners, Set<PhysicalCard> losers, Set<PhysicalCard> removedFromSkirmish) {
        super(type);
        _winners = winners;
        _inSkirmishlosers = losers;
        _losers = new HashSet<>(losers);
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
