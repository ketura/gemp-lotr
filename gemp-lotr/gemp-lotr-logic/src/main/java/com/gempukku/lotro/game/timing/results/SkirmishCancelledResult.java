package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

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
