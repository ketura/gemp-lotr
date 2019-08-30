package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import org.json.simple.JSONObject;

public class ModifyOwnCost implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "amount", "condition");

        final ValueSource amountSource = ValueResolver.resolveEvaluator(value.get("amount"), 0, environment);
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(value.get("condition"), "condition");

        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        blueprint.appendTwilightCostModifier(
                (game, card) -> {
                    for (Requirement requirement : requirements) {
                        if (!requirement.accepts(null, null, game, card, null, null))
                            return 0;
                    }

                    final Evaluator evaluator = amountSource.getEvaluator(null, null, game, card, null, null);
                    return evaluator.evaluateExpression(game, card);
                });
    }
}
