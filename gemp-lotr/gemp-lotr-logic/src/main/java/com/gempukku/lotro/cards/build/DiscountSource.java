package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.effects.DiscountEffect;
import com.gempukku.lotro.game.DefaultGame;

public interface DiscountSource {
    int getPotentialDiscount(DefaultActionContext<DefaultGame> actionContext);

    DiscountEffect getDiscountEffect(CostToEffectAction action, DefaultActionContext actionContext);
}
