package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class NormalSkirmishResult extends SkirmishResult {
    public NormalSkirmishResult(List<PhysicalCard> winners, List<PhysicalCard> losers) {
        super(EffectResult.Type.RESOLVE_SKIRMISH, winners, losers);
    }
}
