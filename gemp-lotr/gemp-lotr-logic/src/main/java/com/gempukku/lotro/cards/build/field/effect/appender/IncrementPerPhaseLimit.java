package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.IncrementPhaseLimitEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import org.json.simple.JSONObject;

public class IncrementPerPhaseLimit implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "limit", "perPlayer");

        final int limit = FieldUtils.getInteger(effectObject.get("limit"), "limit", 1);
        final boolean perPlayer = FieldUtils.getBoolean(effectObject.get("perPlayer"), "perPlayer", false);

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                if (perPlayer)
                    return new IncrementPhaseLimitEffect(actionContext.getSource(), actionContext.getPerformingPlayer() + "_", limit);
                else
                    return new IncrementPhaseLimitEffect(actionContext.getSource(), limit);
            }

            @Override
            public boolean isPlayableInFull(ActionContext actionContext) {
                if (perPlayer)
                    return PlayConditions.checkPhaseLimit(actionContext.getGame(), actionContext.getSource(), actionContext.getPerformingPlayer() + "_", limit);
                else
                    return PlayConditions.checkPhaseLimit(actionContext.getGame(), actionContext.getSource(), limit);
            }
        };
    }

}
