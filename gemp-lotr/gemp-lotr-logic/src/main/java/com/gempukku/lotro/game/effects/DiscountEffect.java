package com.gempukku.lotro.game.effects;

import com.gempukku.lotro.game.DefaultGame;

public interface DiscountEffect extends Effect {
    int getMaximumPossibleDiscount(DefaultGame game);

    void setMinimalRequiredDiscount(int minimalDiscount);

    int getDiscountPaidFor();
}
