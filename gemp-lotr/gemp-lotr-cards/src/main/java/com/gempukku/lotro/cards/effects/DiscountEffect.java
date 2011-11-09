package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.logic.timing.Effect;

public interface DiscountEffect extends Effect {
    public void setMinimalRequiredDiscount(int minimalDiscount);

    public int getDiscountPaidFor();
}
