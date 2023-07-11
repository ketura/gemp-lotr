package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.modifiers.ExtraPlayCost;

public interface ExtraPlayCostSource {
    ExtraPlayCost getExtraPlayCost(ActionContext actionContext);
}
