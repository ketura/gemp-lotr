package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import org.json.simple.JSONObject;

public class ModifyArcheryTotal implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "side", "condition", "amount");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(object.get("amount"), 1, environment);
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("condition"), "condition");
        final Side side = FieldUtils.getEnum(Side.class, object.get("side"), "side");

        if (side == null)
            throw new InvalidCardDefinitionException("This modifier requires a side");

        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return (actionContext) -> {
            final Evaluator evaluator = valueSource.getEvaluator(actionContext);
            return new ArcheryTotalModifier(actionContext.getSource(), side,
                    new RequirementCondition(requirements, actionContext), evaluator);
        };
    }
}
