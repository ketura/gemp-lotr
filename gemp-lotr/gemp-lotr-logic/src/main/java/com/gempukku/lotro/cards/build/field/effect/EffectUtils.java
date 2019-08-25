package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayRequirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.trigger.TriggerActionSource;
import com.gempukku.lotro.cards.build.field.effect.trigger.TriggerChecker;
import org.json.simple.JSONObject;

public class EffectUtils {
    public static TriggerActionSource getTriggerActionSource(JSONObject value, TriggerChecker triggerChecker, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        TriggerActionSource triggerActionSource = new TriggerActionSource();
        triggerActionSource.setTriggerChecker(triggerChecker);

        processRequirementsCostsAndEffects(value, environment, triggerActionSource);
        return triggerActionSource;
    }

    public static void processRequirementsCostsAndEffects(JSONObject value, CardGenerationEnvironment environment, DefaultActionSource triggerActionSource) throws InvalidCardDefinitionException {
        final JSONObject[] requirementArray = FieldUtils.getObjectArray(value.get("requirement"), "requirement");
        for (JSONObject requirement : requirementArray) {
            final PlayRequirement playRequirement = environment.getRequirementFactory().getRequirement(requirement, environment);
            triggerActionSource.addPlayRequirement(playRequirement);
        }

        final JSONObject[] costArray = FieldUtils.getObjectArray(value.get("cost"), "cost");
        for (JSONObject cost : costArray) {
            final EffectAppender effectAppender = environment.getEffectAppenderFactory().getEffectAppender(cost, environment);
            triggerActionSource.addCost(effectAppender);
        }

        final JSONObject[] effectArray = FieldUtils.getObjectArray(value.get("effect"), "effect");
        for (JSONObject effect : effectArray) {
            final EffectAppender effectAppender = environment.getEffectAppenderFactory().getEffectAppender(effect, environment);
            triggerActionSource.addEffect(effectAppender);
        }
    }
}
