package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscountEffect;

public interface DiscountSource {
    int getPotentialDiscount(ActionContext actionContext);

    DiscountEffect getDiscountEffect(CostToEffectAction action, ActionContext actionContext);
}
