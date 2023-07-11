package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.timing.EffectResult;

import java.util.Set;

public class ZeroVitalityResult extends EffectResult {
    private final Set<PhysicalCard> _characters;

    public ZeroVitalityResult(Set<PhysicalCard> characters) {
        super(Type.ZERO_VITALITY);
        _characters = characters;
    }

    public Set<PhysicalCard> getCharacters() {
        return _characters;
    }
}
