package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class SkirmishCancelledResult extends EffectResult {
    public PhysicalCard fpCharacter;

    public SkirmishCancelledResult(PhysicalCard fpCharacter) {
        super(Type.SKIRMISH_CANCELLED);
        this.fpCharacter = fpCharacter;
    }

    public PhysicalCard getFpCharacter() {
        return fpCharacter;
    }
}
