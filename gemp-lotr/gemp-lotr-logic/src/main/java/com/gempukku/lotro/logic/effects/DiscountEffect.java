package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.logic.actions.AbstractCostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;

public interface DiscountEffect extends Effect {
    public void setMinimalRequiredDiscount(int minimalDiscount);

    public int getDiscountPaidFor();

    public void afterDiscountCallback(AbstractCostToEffectAction action);
}
