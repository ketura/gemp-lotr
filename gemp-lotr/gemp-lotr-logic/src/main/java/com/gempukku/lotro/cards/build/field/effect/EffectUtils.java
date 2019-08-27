package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.trigger.TriggerChecker;
import org.json.simple.JSONObject;

public class EffectUtils {
    public static DefaultActionSource getTriggerActionSource(JSONObject value, TriggerChecker triggerChecker, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        DefaultActionSource triggerActionSource = new DefaultActionSource();
        triggerActionSource.addPlayRequirement(triggerChecker);

        processRequirementsCostsAndEffects(value, environment, triggerActionSource);
        return triggerActionSource;
    }

    public static void processRequirementsCostsAndEffects(JSONObject value, CardGenerationEnvironment environment, DefaultActionSource actionSource) throws InvalidCardDefinitionException {
        final JSONObject[] requirementArray = FieldUtils.getObjectArray(value.get("requirement"), "requirement");
        for (JSONObject requirement : requirementArray) {
            final Requirement conditionRequirement = environment.getRequirementFactory().getRequirement(requirement, environment);
            actionSource.addPlayRequirement(conditionRequirement);
        }

        processCostsAndEffects(value, environment, actionSource);
    }

    public static void processCostsAndEffects(JSONObject value, CardGenerationEnvironment environment, DefaultActionSource actionSource) throws InvalidCardDefinitionException {
        final JSONObject[] costArray = FieldUtils.getObjectArray(value.get("cost"), "cost");
        for (JSONObject cost : costArray) {
            final EffectAppender effectAppender = environment.getEffectAppenderFactory().getEffectAppender(cost, environment);
            actionSource.addCost(effectAppender);
        }

        final JSONObject[] effectArray = FieldUtils.getObjectArray(value.get("effect"), "effect");
        for (JSONObject effect : effectArray) {
            final EffectAppender effectAppender = environment.getEffectAppenderFactory().getEffectAppender(effect, environment);
            actionSource.addEffect(effectAppender);
        }
    }
}
