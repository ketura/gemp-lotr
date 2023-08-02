package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.choose.ChoosePlayerEffect;
import org.json.simple.JSONObject;

public class ChoosePlayer implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "memorize");

        final String memorize = FieldUtils.getString(effectObject.get("memorize"), "memorize");

        return new DelayedAppender<>() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                return new ChoosePlayerEffect(actionContext.getPerformingPlayer()) {
                    @Override
                    protected void playerChosen(String playerId) {
                        actionContext.setValueToMemory(memorize, playerId);
                    }
                };
            }
        };
    }

}
