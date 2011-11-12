package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;
import java.util.Set;

public class NormalSkirmishResult extends SkirmishResult {
    public NormalSkirmishResult(List<PhysicalCard> winners, List<PhysicalCard> losers, Set<PhysicalCard> removedFromSkirmish) {
        super(EffectResult.Type.RESOLVE_SKIRMISH, winners, losers, removedFromSkirmish);
    }
}
