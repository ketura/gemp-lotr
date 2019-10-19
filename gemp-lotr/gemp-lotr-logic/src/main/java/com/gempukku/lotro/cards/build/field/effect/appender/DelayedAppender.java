package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

public abstract class DelayedAppender implements EffectAppender {
    private String text;

    public DelayedAppender() {

    }

    public DelayedAppender(String text) {
        this.text = text;
    }

    @Override
    public final void appendEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
        final UnrespondableEffect effect = new UnrespondableEffect() {
            @Override
            protected void doPlayEffect(LotroGame game) {
                // Need to insert them, but in the reverse order
                final List<? extends Effect> effects = createEffects(cost, action, actionContext);
                if (effects != null) {
                    final Effect[] effectsArray = effects.toArray(new Effect[0]);
                    for (int i = effectsArray.length - 1; i >= 0; i--)
                        if (cost)
                            action.insertCost(effectsArray[i]);
                        else
                            action.insertEffect(effectsArray[i]);
                }
            }

            @Override
            public String getText(LotroGame game) {
                return text;
            }
        };

        if (cost) {
            action.appendCost(effect);
        } else {
            action.appendEffect(effect);
        }
    }

    protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
        throw new UnsupportedOperationException("One of createEffect or createEffects has to be overwritten");
    }

    protected List<? extends Effect> createEffects(boolean cost, CostToEffectAction action, ActionContext actionContext) {
        final Effect effect = createEffect(cost, action, actionContext);
        if (effect == null)
            return null;
        return Collections.singletonList(effect);
    }

    @Override
    public boolean isPlayableInFull(ActionContext actionContext) {
        return true;
    }
}
