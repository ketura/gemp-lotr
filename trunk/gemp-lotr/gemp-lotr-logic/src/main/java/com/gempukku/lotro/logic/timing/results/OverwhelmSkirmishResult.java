package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Set;

public class OverwhelmSkirmishResult extends SkirmishResult {
    public OverwhelmSkirmishResult(Set<PhysicalCard> winners, Set<PhysicalCard> losers, Set<PhysicalCard> removedFromSkirmish) {
        super(EffectResult.Type.SKIRMISH_FINISHED_WITH_OVERWHELM, winners, losers, removedFromSkirmish);
    }
}
