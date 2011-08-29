package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public abstract class SkirmishResult extends EffectResult {
    private List<PhysicalCard> _winners;
    private List<PhysicalCard> _losers;

    public SkirmishResult(EffectResult.Type type, List<PhysicalCard> winners, List<PhysicalCard> losers) {
        super(type);
        _winners = winners;
        _losers = losers;
    }

    public List<PhysicalCard> getWinners() {
        return _winners;
    }

    public List<PhysicalCard> getLosers() {
        return _losers;
    }
}
