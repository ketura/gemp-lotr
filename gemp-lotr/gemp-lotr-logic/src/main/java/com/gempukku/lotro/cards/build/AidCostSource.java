package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;

public interface AidCostSource {
    boolean canPayAidCost(DefaultActionContext actionContext);

    void appendAidCost(CostToEffectAction action, DefaultActionContext actionContext);
}
