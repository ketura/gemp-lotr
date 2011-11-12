package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;
import java.util.Set;

public class OverwhelmSkirmishResult extends SkirmishResult {
    public OverwhelmSkirmishResult(List<PhysicalCard> winners, List<PhysicalCard> losers, Set<PhysicalCard> removedFromSkirmish) {
        super(EffectResult.Type.OVERWHELM_IN_SKIRMISH, winners, losers, removedFromSkirmish);
    }
}
