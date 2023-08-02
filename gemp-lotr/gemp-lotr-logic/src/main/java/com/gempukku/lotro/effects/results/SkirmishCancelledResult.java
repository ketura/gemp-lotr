package com.gempukku.lotro.effects.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.effects.EffectResult;

public class SkirmishCancelledResult extends EffectResult {
    public LotroPhysicalCard fpCharacter;

    public SkirmishCancelledResult(LotroPhysicalCard fpCharacter) {
        super(Type.SKIRMISH_CANCELLED);
        this.fpCharacter = fpCharacter;
    }

    public LotroPhysicalCard getFpCharacter() {
        return fpCharacter;
    }
}
