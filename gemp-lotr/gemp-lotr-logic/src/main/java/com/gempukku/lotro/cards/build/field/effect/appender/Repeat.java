package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.StackActionEffect;
import com.gempukku.lotro.logic.timing.Effect;
import org.json.simple.JSONObject;

public class Repeat implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "amount", "effect");

        final ValueSource amountSource = ValueResolver.resolveEvaluator(effectObject.get("amount"), environment);
        final JSONObject effect = (JSONObject) effectObject.get("effect");

        final EffectAppender effectAppender = environment.getEffectAppenderFactory().getEffectAppender(effect, environment);

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                final int count = amountSource.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
                if (count > 0) {
                    SubAction subAction = new SubAction(action);
                    for (int i = 0; i < count; i++)
                        effectAppender.appendEffect(cost, subAction, actionContext);
                    return new StackActionEffect(subAction);
                } else {
                    return null;
                }
            }

            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                return effectAppender.isPlayableInFull(actionContext);
            }

            @Override
            public boolean isPlayabilityCheckedForEffect() {
                return effectAppender.isPlayabilityCheckedForEffect();
            }
        };
    }
}
