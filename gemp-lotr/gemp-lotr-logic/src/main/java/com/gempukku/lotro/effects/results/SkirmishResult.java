package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

import java.util.HashSet;
import java.util.Set;

public abstract class SkirmishResult extends EffectResult {
    private final Set<LotroPhysicalCard> _winners;
    private final Set<LotroPhysicalCard> _inSkirmishlosers;
    private final Set<LotroPhysicalCard> _losers;

    public SkirmishResult(EffectResult.Type type, Set<LotroPhysicalCard> winners, Set<LotroPhysicalCard> losers, Set<LotroPhysicalCard> removedFromSkirmish) {
        super(type);
        _winners = winners;
        _inSkirmishlosers = losers;
        _losers = new HashSet<>(losers);
        _losers.addAll(removedFromSkirmish);
    }

    public Set<LotroPhysicalCard> getWinners() {
        return _winners;
    }

    public Set<LotroPhysicalCard> getInSkirmishLosers() {
        return _inSkirmishlosers;
    }

    public Set<LotroPhysicalCard> getLosers() {
        return _losers;
    }
}
