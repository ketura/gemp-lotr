package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;

public interface EffectAppender {
    void appendEffect(boolean cost, CostToEffectAction action, ActionContext actionContext);

    boolean isPlayableInFull(ActionContext actionContext);

    default boolean isPlayabilityCheckedForEffect() {
        return false;
    }
}
