package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.PhysicalCard;

public interface TwilightCostModifierSource {
    int getTwilightCostModifier(ActionContext actionContext, PhysicalCard target);
}
