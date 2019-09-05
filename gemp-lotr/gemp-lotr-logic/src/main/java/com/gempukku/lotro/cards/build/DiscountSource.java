package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.logic.effects.DiscountEffect;

public interface DiscountSource {
    int getPotentialDiscount(ActionContext actionContext);

    DiscountEffect getDiscountEffect(ActionContext actionContext);
}
