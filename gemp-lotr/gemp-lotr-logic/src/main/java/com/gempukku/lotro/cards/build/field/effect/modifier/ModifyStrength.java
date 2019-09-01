package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import org.json.simple.JSONObject;

public class ModifyStrength implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "condition", "amount");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(value.get("amount"), 1, environment);
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(value.get("condition"), "condition");
        final String filter = FieldUtils.getString(value.get("filter"), "filter", "self");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        blueprint.appendInPlayModifier(
                (actionContext) -> {
                    final Evaluator evaluator = valueSource.getEvaluator(actionContext);
                    return new StrengthModifier(actionContext.getSource(),
                            filterableSource.getFilterable(actionContext),
                            new RequirementCondition(requirements, actionContext), evaluator);
                });
    }
}
