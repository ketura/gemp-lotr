package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.ExtraPlayCost;

public interface ExtraPlayCostSource {
    ExtraPlayCost getExtraPlayCost(ActionContext actionContext);
}
