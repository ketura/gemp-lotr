package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

public interface DiscountEffect extends Effect {
    int getMaximumPossibleDiscount(LotroGame game);

    void setMinimalRequiredDiscount(int minimalDiscount);

    int getDiscountPaidFor();
}
