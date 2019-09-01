package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;

public abstract class AbstractEffectAppender implements EffectAppender {
    @Override
    public final void appendEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
        action.appendEffect(createEffect(cost, action, actionContext));
    }

    protected abstract Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext);
}
