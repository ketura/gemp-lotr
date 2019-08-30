package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import org.json.simple.JSONObject;

public class CantTakeMoreWoundsThan implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "phase", "condition", "amount");

        final ValueSource valueSource = ValueResolver.resolveEvaluator(value.get("amount"), 1, environment);
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(value.get("condition"), "condition");
        final String filter = FieldUtils.getString(value.get("filter"), "filter", "self");
        final Phase phase = FieldUtils.getEnum(Phase.class, value.get("phase"), "phase");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        blueprint.appendInPlayModifier(
                (game, self) -> {
                    final Evaluator evaluator = valueSource.getEvaluator(null, null, game, self, null, null);
                    return new CantTakeMoreThanXWoundsModifier(self, phase,
                            valueSource.getEvaluator(null, null, game, self, null, null)
                                    .evaluateExpression(game, self),
                            new RequirementCondition(requirements, null, self, null, null),
                            filterableSource.getFilterable(null, null, game, self, null, null));
                });

    }
}
