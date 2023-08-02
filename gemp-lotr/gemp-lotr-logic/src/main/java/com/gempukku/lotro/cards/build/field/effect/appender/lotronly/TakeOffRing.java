package com.gempukku.lotro.cards.build.field.effect.appender.lotronly;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.appender.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.DelayedAppender;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.TakeOffTheOneRingEffect;
import com.gempukku.lotro.game.DefaultGame;
import org.json.simple.JSONObject;

public class TakeOffRing implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject);
        return new DelayedAppender<>() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                return new TakeOffTheOneRingEffect();
            }

            @Override
            public boolean isPlayableInFull(DefaultActionContext<DefaultGame> actionContext) {
                return actionContext.getGame().getGameState().isWearingRing();
            }
        };
    }

}
