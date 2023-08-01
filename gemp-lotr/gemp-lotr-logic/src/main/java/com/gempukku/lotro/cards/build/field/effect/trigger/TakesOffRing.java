package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.effects.EffectResult;
import com.gempukku.lotro.game.DefaultGame;
import org.json.simple.JSONObject;

public class TakesOffRing implements TriggerCheckerProducer {
    @Override
    public TriggerChecker getTriggerChecker(JSONObject value, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value);

        return new TriggerChecker() {
            @Override
            public boolean accepts(DefaultActionContext<DefaultGame> actionContext) {
                return actionContext.getEffectResult().getType() == EffectResult.Type.TAKE_OFF_THE_ONE_RING;
            }

            @Override
            public boolean isBefore() {
                return false;
            }
        };
    }
}
