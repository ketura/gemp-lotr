package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.modifiers.ExtraPlayCost;

public interface ExtraPlayCostSource {
    ExtraPlayCost getExtraPlayCost(ActionContext actionContext);
}
