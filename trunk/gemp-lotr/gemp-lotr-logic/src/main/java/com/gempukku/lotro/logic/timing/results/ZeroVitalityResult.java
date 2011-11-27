package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Set;

public class ZeroVitalityResult extends EffectResult {
    private Set<PhysicalCard> _characters;

    public ZeroVitalityResult(Set<PhysicalCard> characters) {
        super(Type.ZERO_VITALITY);
        _characters = characters;
    }

    public Set<PhysicalCard> getCharacters() {
        return _characters;
    }
}
