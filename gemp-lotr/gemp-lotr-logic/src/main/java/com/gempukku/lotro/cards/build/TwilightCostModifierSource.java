package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.cards.PhysicalCard;

public interface TwilightCostModifierSource {
    int getTwilightCostModifier(ActionContext actionContext, PhysicalCard target);
}
