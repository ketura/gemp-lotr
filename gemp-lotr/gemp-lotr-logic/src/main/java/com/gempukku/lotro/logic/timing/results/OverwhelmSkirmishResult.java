package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

public class OverwhelmSkirmishResult extends SkirmishResult {
    public OverwhelmSkirmishResult(List<PhysicalCard> winners, List<PhysicalCard> losers) {
        super(EffectResult.Type.OVERWHELM_IN_SKIRMISH, winners, losers);
    }
}
