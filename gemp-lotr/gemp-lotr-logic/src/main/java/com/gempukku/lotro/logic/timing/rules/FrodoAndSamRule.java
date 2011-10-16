package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;

public class FrodoAndSamRule {
    private ModifiersLogic _modifiersLogic;

    public FrodoAndSamRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        _modifiersLogic.addAlwaysOnModifier(
                new KeywordModifier(null, Filters.or(Filters.name("Frodo"), Filters.name("Sam")), Keyword.RING_BOUND));
    }
}
