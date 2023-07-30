package com.gempukku.lotro.rules.lotronly;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.modifiers.CantPlayCardsModifier;
import com.gempukku.lotro.modifiers.KeywordModifier;
import com.gempukku.lotro.modifiers.ModifiersLogic;

public class RingRelatedRule {
    private final ModifiersLogic _modifiersLogic;

    public RingRelatedRule(ModifiersLogic modifiersLogic) {
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        _modifiersLogic.addAlwaysOnModifier(
                new KeywordModifier(null, Filters.or(Filters.frodo, Filters.sam), Keyword.RING_BOUND));
        _modifiersLogic.addAlwaysOnModifier(
                new CantPlayCardsModifier(null, Filters.frodo, Keyword.CAN_START_WITH_RING));
        _modifiersLogic.addAlwaysOnModifier(
                new KeywordModifier(null, Filters.ringBearer, Keyword.RING_BOUND));
    }
}
