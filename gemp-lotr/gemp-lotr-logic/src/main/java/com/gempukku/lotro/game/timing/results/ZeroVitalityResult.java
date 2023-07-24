package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

import java.util.Set;

public class ZeroVitalityResult extends EffectResult {
    private final Set<LotroPhysicalCard> _characters;

    public ZeroVitalityResult(Set<LotroPhysicalCard> characters) {
        super(Type.ZERO_VITALITY);
        _characters = characters;
    }

    public Set<LotroPhysicalCard> getCharacters() {
        return _characters;
    }
}
