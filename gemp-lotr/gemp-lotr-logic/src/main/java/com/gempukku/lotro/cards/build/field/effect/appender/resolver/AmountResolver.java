package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.EvaluatorSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import org.json.simple.JSONObject;

public class AmountResolver {
    public static EvaluatorSource resolveEvaluator(Object value, int defaultValue, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (value == null)
            return (playerId, game, self, effectResult, effect) -> new ConstantEvaluator(defaultValue);
        if (value instanceof Number)
            return (playerId, game, self, effectResult, effect) -> new ConstantEvaluator(((Number) value).intValue());

        if (value instanceof JSONObject) {
            JSONObject object = (JSONObject) value;
            final String type = FieldUtils.getString(object.get("type"), "type");
            if (type.equalsIgnoreCase("condition")) {
                final Requirement condition = environment.getRequirementFactory().getRequirement((JSONObject) object.get("condition"), environment);
                int trueValue = FieldUtils.getInteger(object.get("true"), "true");
                int falseValue = FieldUtils.getInteger(object.get("false"), "false");
                return (playerId, game, self, effectResult, effect) -> (Evaluator) (game1, cardAffected) -> {
                    final boolean accepts = condition.accepts(playerId, game1, self, effectResult, effect);
                    if (accepts)
                        return trueValue;
                    else
                        return falseValue;
                };
            }
            throw new InvalidCardDefinitionException("Unrecognized type of an evaluator " + type);
        }
        throw new InvalidCardDefinitionException("Unable to resolve an evaluator");
    }
}
