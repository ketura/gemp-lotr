package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.modifiers.CantDiscardFromPlayModifier;
import com.gempukku.lotro.modifiers.CantReturnToHandModifier;
import com.gempukku.lotro.modifiers.ModifiersLogic;

public class RingBearerRule {
    private final ModifiersLogic _modifiersLogic;

    public RingBearerRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        _modifiersLogic.addAlwaysOnModifier(
                new CantDiscardFromPlayModifier(null, "Can't be discarded from play", Filters.ringBearer, Filters.any));
        _modifiersLogic.addAlwaysOnModifier(
                new CantReturnToHandModifier(null, "Can't be returned to hand", Filters.ringBearer, Filters.any));
    }
}
