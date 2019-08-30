package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import org.json.simple.JSONObject;

public class EffectUtils {
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
        final EffectAppenderFactory effectAppenderFactory = environment.getEffectAppenderFactory();
        for (JSONObject cost : costArray) {
            final EffectAppender effectAppender = effectAppenderFactory.getEffectAppender(cost, environment);
            actionSource.addPlayRequirement(
                    (action, playerId, game, self, effectResult, effect) -> effectAppender.isPlayableInFull(action, playerId, game, self, effectResult, effect));
            actionSource.addCost(effectAppender);
        }

        final JSONObject[] effectArray = FieldUtils.getObjectArray(value.get("effect"), "effect");
        for (JSONObject effect : effectArray) {
            final EffectAppender effectAppender = effectAppenderFactory.getEffectAppender(effect, environment);
            if (effectAppender == null) {
                System.out.println("");
            }
            if (effectAppender.isPlayabilityCheckedForEffect())
                actionSource.addPlayRequirement(
                        (action, playerId, game, self, effectResult, effect1) -> effectAppender.isPlayableInFull(action, playerId, game, self, effectResult, effect1));
            actionSource.addEffect(effectAppender);
        }
    }
}
