package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.modifiers.TwilightCostModifier;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;
import org.json.simple.JSONObject;

public class ModifyCost implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "requires", "filter", "amount");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("requires"), "requires");
        final String filter = FieldUtils.getString(object.get("filter"), "filter");

        final Requirement[] conditions = environment.getRequirementFactory().getRequirements(conditionArray, environment);
        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
        final ValueSource amountSource = ValueResolver.resolveEvaluator(object.get("amount"), environment);

        return actionContext -> {
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    final RequirementCondition requirementCondition = new RequirementCondition(conditions, actionContext);
                    final Evaluator evaluator = amountSource.getEvaluator(actionContext);
                    return new TwilightCostModifier(actionContext.getSource(), filterable, requirementCondition, evaluator);
        };
    }
}
