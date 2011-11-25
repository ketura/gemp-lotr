package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

public class ZeroVitalityResult extends EffectResult {
    private PhysicalCard _character;

    public ZeroVitalityResult(PhysicalCard character) {
        super(Type.ZERO_VITALITY);
        _character = character;
    }

    public PhysicalCard getCharacter() {
        return _character;
    }
}
