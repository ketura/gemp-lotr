package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.modifiers.CantTakeMoreThanXWoundsModifier;
import org.json.simple.JSONObject;

public class CantTakeMoreWoundsThan implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "phase", "condition", "amount");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(value.get("amount"), 1, environment);
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(value.get("condition"), "condition");
        final String filter = FieldUtils.getString(value.get("filter"), "filter", "self");
        final Phase phase = FieldUtils.getEnum(Phase.class, value.get("phase"), "phase");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        blueprint.appendInPlayModifier(
                (actionContext) -> {
                    return new CantTakeMoreThanXWoundsModifier(actionContext.getSource(), phase,
                            valueSource.getEvaluator(actionContext)
                                    .evaluateExpression(actionContext.getGame(), null),
                            new RequirementCondition(requirements, actionContext),
                            filterableSource.getFilterable(actionContext));
                });

    }
}
