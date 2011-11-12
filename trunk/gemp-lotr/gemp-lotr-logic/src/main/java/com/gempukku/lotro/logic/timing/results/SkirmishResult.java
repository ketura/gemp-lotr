package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class SkirmishResult extends EffectResult {
    private List<PhysicalCard> _winners;
    private List<PhysicalCard> _inSkirmishlosers;
    private List<PhysicalCard> _losers;

    public SkirmishResult(EffectResult.Type type, List<PhysicalCard> winners, List<PhysicalCard> losers, Set<PhysicalCard> removedFromSkirmish) {
        super(type);
        _winners = winners;
        _inSkirmishlosers = losers;
        _losers = new LinkedList<PhysicalCard>(losers);
        _losers.addAll(removedFromSkirmish);
    }

    public List<PhysicalCard> getWinners() {
        return _winners;
    }

    public List<PhysicalCard> getInSkirmishLosers() {
        return _inSkirmishlosers;
    }

    public List<PhysicalCard> getLosers() {
        return _losers;
    }
}
