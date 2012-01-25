package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Set;

public class NormalSkirmishResult extends SkirmishResult {
    public NormalSkirmishResult(Set<PhysicalCard> winners, Set<PhysicalCard> losers, Set<PhysicalCard> removedFromSkirmish) {
        super(EffectResult.Type.SKIRMISH_FINISHED_NORMALLY, winners, losers, removedFromSkirmish);
    }
}
