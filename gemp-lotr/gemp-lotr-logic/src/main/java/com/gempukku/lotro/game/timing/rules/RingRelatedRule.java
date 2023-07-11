package com.gempukku.lotro.game.timing.rules;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.modifiers.CantPlayCardsModifier;
import com.gempukku.lotro.game.modifiers.KeywordModifier;
import com.gempukku.lotro.game.modifiers.ModifiersLogic;

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
