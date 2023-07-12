package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.effects.RemoveTwilightEffect;
import com.gempukku.lotro.game.effects.Effect;
import org.json.simple.JSONObject;

public class RemoveTwilight implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "amount");
        final ValueSource amount = ValueResolver.resolveEvaluator(effectObject.get("amount"), 1, environment);

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                int value = amount.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
                return new RemoveTwilightEffect(value);
            }

            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                int value = amount.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
                return actionContext.getGame().getGameState().getTwilightPool() >= value;
            }
        };
    }
}
