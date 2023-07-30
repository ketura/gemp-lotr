package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

import java.util.Set;

public class NormalSkirmishResult extends SkirmishResult {
    public NormalSkirmishResult(Set<LotroPhysicalCard> winners, Set<LotroPhysicalCard> losers, Set<LotroPhysicalCard> removedFromSkirmish) {
        super(EffectResult.Type.SKIRMISH_FINISHED_NORMALLY, winners, losers, removedFromSkirmish);
    }
}
