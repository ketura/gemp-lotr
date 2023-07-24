package com.gempukku.lotro.game.timing.results;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.effects.EffectResult;

import java.util.Set;

public class SkirmishAboutToEndResult extends EffectResult {
    private final Set<LotroPhysicalCard> _minionsInvolved;

    public SkirmishAboutToEndResult(Set<LotroPhysicalCard> minionsInvolved) {
        super(EffectResult.Type.SKIRMISH_ABOUT_TO_END);
	_minionsInvolved = minionsInvolved;
    }

    public Set<LotroPhysicalCard> getMinionsInvolved() {
        return _minionsInvolved;
    }
}
