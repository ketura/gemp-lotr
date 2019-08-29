package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import org.json.simple.JSONObject;

public class ValueResolver {
    public static ValueSource resolveEvaluator(Object value, int defaultValue, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (value == null)
            return new ConstantEvaluator(defaultValue);
        if (value instanceof Number)
            return new ConstantEvaluator(((Number) value).intValue());
        if (value instanceof String) {
            String stringValue = (String) value;
            if (stringValue.contains("-")) {
                final String[] split = stringValue.split("-", 2);
                final int min = Integer.parseInt(split[0]);
                final int max = Integer.parseInt(split[1]);
                if (min > max || min < 0 || max < 1)
                    throw new InvalidCardDefinitionException("Unable to resolve count: " + value);
                return new ValueSource() {
                    @Override
                    public Evaluator getEvaluator(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        throw new RuntimeException("Evaluator has resolved to range");
                    }

                    @Override
                    public int getMinimum(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        return min;
                    }

                    @Override
                    public int getMaximum(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        return max;
                    }
                };
            } else {
                int v = Integer.parseInt(stringValue);
                return new ConstantEvaluator(v);
            }
        }
        if (value instanceof JSONObject) {
            JSONObject object = (JSONObject) value;
            final String type = FieldUtils.getString(object.get("type"), "type");
            if (type.equalsIgnoreCase("condition")) {
                final Requirement condition = environment.getRequirementFactory().getRequirement((JSONObject) object.get("condition"), environment);
                int trueValue = FieldUtils.getInteger(object.get("true"), "true");
                int falseValue = FieldUtils.getInteger(object.get("false"), "false");
                return (action, playerId, game, self, effectResult, effect) -> (Evaluator) (game1, cardAffected) -> {
                    final boolean accepts = condition.accepts(action, playerId, game1, self, effectResult, effect);
                    if (accepts)
                        return trueValue;
                    else
                        return falseValue;
                };
            } else if (type.equalsIgnoreCase("forEachInMemory")) {
                final String memory = FieldUtils.getString(object.get("memory"), "memory");
                return (action, playerId, game, self, effectResult, effect) -> {
                    final int count = action.getCardsFromMemory(memory).size();
                    return new ConstantEvaluator(count);
                };
            }
            throw new InvalidCardDefinitionException("Unrecognized type of an evaluator " + type);
        }
        throw new InvalidCardDefinitionException("Unable to resolve an evaluator");
    }
}
