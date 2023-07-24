package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;

public interface TwilightCostModifierSource {
    int getTwilightCostModifier(ActionContext actionContext, LotroPhysicalCard target);
}
