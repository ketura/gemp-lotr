package com.gempukku.lotro.cards.build.field.effect.modifier.lotronly;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.cards.build.field.effect.modifier.ModifierSourceProducer;
import com.gempukku.lotro.cards.build.field.effect.modifier.RequirementCondition;
import com.gempukku.lotro.modifiers.FPCulturesSpotCountModifier;
import org.json.simple.JSONObject;

public class FPCultureSpot implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "requires", "amount");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("requires"), "requires");
        final ValueSource amount = ValueResolver.resolveEvaluator(object.get("amount"), environment);

        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return actionContext -> new FPCulturesSpotCountModifier(actionContext.getSource(),
                new RequirementCondition(requirements, actionContext),
                amount.getEvaluator(actionContext));
    }
}
