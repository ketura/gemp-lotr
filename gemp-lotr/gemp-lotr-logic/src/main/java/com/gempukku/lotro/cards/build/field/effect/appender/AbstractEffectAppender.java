package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.game.actions.CostToEffectAction;
import com.gempukku.lotro.game.timing.Effect;

public abstract class AbstractEffectAppender implements EffectAppender {
    @Override
    public final void appendEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
        if (cost)
            action.appendCost(createEffect(cost, action, actionContext));
        else
            action.appendEffect(createEffect(cost, action, actionContext));
    }

    protected abstract Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext);

    @Override
    public boolean isPlayableInFull(ActionContext actionContext) {
        return true;
    }
}
