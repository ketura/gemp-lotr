package com.gempukku.lotro.logic.timing.results;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Set;

public class SkirmishAboutToEndResult extends EffectResult {
    private final Set<PhysicalCard> _minionsInvolved;

    public SkirmishAboutToEndResult(Set<PhysicalCard> minionsInvolved) {
        super(EffectResult.Type.SKIRMISH_ABOUT_TO_END);
	_minionsInvolved = minionsInvolved;
    }

    public Set<PhysicalCard> getMinionsInvolved() {
        return _minionsInvolved;
    }
}
