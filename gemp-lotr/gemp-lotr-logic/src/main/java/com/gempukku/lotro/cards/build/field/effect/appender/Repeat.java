package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.actions.lotronly.SubAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.StackActionEffect;
import com.gempukku.lotro.game.DefaultGame;
import org.json.simple.JSONObject;

public class Repeat implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "amount", "effect");

        final ValueSource amountSource = ValueResolver.resolveEvaluator(effectObject.get("amount"), environment);
        final JSONObject effect = (JSONObject) effectObject.get("effect");

        final EffectAppender effectAppender = environment.getEffectAppenderFactory().getEffectAppender(effect, environment);

        return new DelayedAppender<>() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
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
            public boolean isPlayableInFull(DefaultActionContext<DefaultGame> actionContext) {
                return effectAppender.isPlayableInFull(actionContext);
            }

            @Override
            public boolean isPlayabilityCheckedForEffect() {
                return effectAppender.isPlayabilityCheckedForEffect();
            }
        };
    }
}
