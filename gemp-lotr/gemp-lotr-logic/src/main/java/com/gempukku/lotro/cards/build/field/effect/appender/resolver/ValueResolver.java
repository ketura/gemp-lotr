package com.gempukku.lotro.cards.build.field.effect.appender.resolver;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.evaluator.*;
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
                    public Evaluator getEvaluator(ActionContext actionContext) {
                        throw new RuntimeException("Evaluator has resolved to range");
                    }

                    @Override
                    public int getMinimum(ActionContext actionContext) {
                        return min;
                    }

                    @Override
                    public int getMaximum(ActionContext actionContext) {
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
                FieldUtils.validateAllowedFields(object, "condition", "true", "false");
                final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("condition"), "condition");
                final Requirement[] conditions = environment.getRequirementFactory().getRequirements(conditionArray, environment);
                int trueValue = FieldUtils.getInteger(object.get("true"), "true");
                int falseValue = FieldUtils.getInteger(object.get("false"), "false");
                return (actionContext) -> (Evaluator) (game, cardAffected) -> {
                    for (Requirement condition : conditions) {
                        if (!condition.accepts(actionContext))
                            return falseValue;
                    }
                    return trueValue;
                };
            } else if (type.equalsIgnoreCase("forRegionNumber")) {
                FieldUtils.validateAllowedFields(object);
                return (actionContext) -> (Evaluator) (game, cardAffected) -> GameUtils.getRegion(actionContext.getGame());
            } else if (type.equalsIgnoreCase("forEachInMemory")) {
                FieldUtils.validateAllowedFields(object, "memory");
                final String memory = FieldUtils.getString(object.get("memory"), "memory");
                return (actionContext) -> {
                    final int count = actionContext.getCardsFromMemory(memory).size();
                    return new ConstantEvaluator(count);
                };
            } else if (type.equalsIgnoreCase("forEachThreat")) {
                FieldUtils.validateAllowedFields(object, "multiplier");
                final int multiplier = FieldUtils.getInteger(object.get("multiplier"), "multiplier", 1);
                return actionContext -> new MultiplyEvaluator(multiplier, new ForEachThreatEvaluator());
            } else if (type.equalsIgnoreCase("forEachWound")) {
                FieldUtils.validateAllowedFields(object, "filter", "multiplier");
                final String filter = FieldUtils.getString(object.get("filter"), "filter");
                final int multiplier = FieldUtils.getInteger(object.get("multiplier"), "multiplier", 1);
                final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
                return (actionContext) -> (Evaluator) (game, cardAffected) -> {
                    int wounds = 0;
                    for (PhysicalCard physicalCard : Filters.filterActive(actionContext.getGame(), filterableSource.getFilterable(actionContext))) {
                        wounds += actionContext.getGame().getGameState().getWounds(physicalCard);
                    }
                    return multiplier * wounds;
                };
            } else if (type.equalsIgnoreCase("forEachKeywordOnCardInMemory")) {
                FieldUtils.validateAllowedFields(object, "memory", "keyword");
                final String memory = FieldUtils.getString(object.get("memory"), "memory");
                final Keyword keyword = FieldUtils.getEnum(Keyword.class, object.get("keyword"), "keyword");
                return (actionContext) -> {
                    final LotroGame game = actionContext.getGame();
                    int count = game.getModifiersQuerying().getKeywordCount(game, actionContext.getCardFromMemory(memory), keyword);
                    return new ConstantEvaluator(count);
                };
            } else if (type.equalsIgnoreCase("limit")) {
                FieldUtils.validateAllowedFields(object, "value", "amount");
                final int limit = FieldUtils.getInteger(object.get("value"), "value");
                ValueSource valueSource = resolveEvaluator(object.get("amount"), 0, environment);
                return (actionContext) -> new LimitEvaluator(valueSource.getEvaluator(actionContext), limit);
            } else if (type.equalsIgnoreCase("countStacked")) {
                FieldUtils.validateAllowedFields(object, "on", "filter");
                final String on = FieldUtils.getString(object.get("on"), "on");
                final String filter = FieldUtils.getString(object.get("filter"), "filter", "any");

                final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
                final FilterableSource onFilter = environment.getFilterFactory().generateFilter(on, environment);

                return (actionContext) -> {
                    final Filterable on1 = onFilter.getFilterable(actionContext);
                    return new CountStackedEvaluator(on1, filterableSource.getFilterable(actionContext));
                };
            } else if (type.equalsIgnoreCase("forEachYouCanSpot")) {
                FieldUtils.validateAllowedFields(object, "filter", "over", "limit", "multiplier");
                final String filter = FieldUtils.getString(object.get("filter"), "filter");
                final int over = FieldUtils.getInteger(object.get("over"), "over", 0);
                final int limit = FieldUtils.getInteger(object.get("limit"), "limit", Integer.MAX_VALUE);
                final int multiplier = FieldUtils.getInteger(object.get("multiplier"), "multiplier", 1);
                final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
                return actionContext -> new MultiplyEvaluator(multiplier, new CountSpottableEvaluator(over, limit, filterableSource.getFilterable(actionContext)));
            } else if (type.equalsIgnoreCase("forEachInDiscard")) {
                FieldUtils.validateAllowedFields(object, "filter", "multiplier");
                final String filter = FieldUtils.getString(object.get("filter"), "filter");
                final int multiplier = FieldUtils.getInteger(object.get("multiplier"), "multiplier", 1);
                final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
                return actionContext -> new MultiplyEvaluator(multiplier, new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        final Filterable filterable = filterableSource.getFilterable(actionContext);
                        int count = 0;
                        for (String player : game.getGameState().getPlayerOrder().getAllPlayers())
                            count += Filters.filter(game.getGameState().getDiscard(player), game, filterable).size();

                        return count;
                    }
                });
            } else if (type.equalsIgnoreCase("forEachFPCultureLessThan")) {
                FieldUtils.validateAllowedFields(object, "amount");
                final int lessThan = FieldUtils.getInteger(object.get("amount"), "amount");
                return actionContext -> {
                    int spottable = GameUtils.getSpottableFPCulturesCount(actionContext.getGame(), actionContext.getPerformingPlayer());
                    int result = Math.max(0, lessThan - spottable);
                    return new ConstantEvaluator(result);
                };
            } else if (type.equalsIgnoreCase("forEachInHand")) {
                FieldUtils.validateAllowedFields(object, "filter");
                final String filter = FieldUtils.getString(object.get("filter"), "filter");
                final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
                return actionContext ->
                        (Evaluator) (game, cardAffected) -> Filters.filter(game.getGameState().getHand(actionContext.getPerformingPlayer()),
                                game, filterableSource.getFilterable(actionContext)).size();
            } else if (type.equalsIgnoreCase("fromMemory")) {
                FieldUtils.validateAllowedFields(object, "memory");
                String memory = FieldUtils.getString(object.get("memory"), "memory");
                return (actionContext) -> {
                    int value1 = Integer.parseInt(actionContext.getValueFromMemory(memory));
                    return new ConstantEvaluator(value1);
                };
            } else if (type.equalsIgnoreCase("multiply")) {
                FieldUtils.validateAllowedFields(object, "multiplier", "source");
                final int multiplier = FieldUtils.getInteger(object.get("multiplier"), "multiplier");
                final ValueSource valueSource = ValueResolver.resolveEvaluator(object.get("source"), 0, environment);
                return (actionContext) -> new MultiplyEvaluator(multiplier, valueSource.getEvaluator(actionContext));
            } else if (type.equalsIgnoreCase("forEachVitality")) {
                FieldUtils.validateAllowedFields(object, "multiplier", "over", "filter");
                final int multiplier = FieldUtils.getInteger(object.get("multiplier"), "multiplier", 1);
                final int over = FieldUtils.getInteger(object.get("over"), "over", 0);
                final String filter = FieldUtils.getString(object.get("filter"), "filter", "any");

                final FilterableSource vitalitySource = environment.getFilterFactory().generateFilter(filter, environment);

                return (actionContext) -> {
                    if (filter.equals("any")) {
                        return new MultiplyEvaluator(multiplier,
                                (game, cardAffected) -> Math.max(0, game.getModifiersQuerying().getVitality(game, cardAffected) - over));
                    } else {
                        return new MultiplyEvaluator(multiplier,
                                (game, cardAffected) -> {
                                    final Filterable filterable = vitalitySource.getFilterable(actionContext);
                                    int vitality = 0;
                                    for (PhysicalCard physicalCard : Filters.filterActive(game, filterable)) {
                                        vitality += game.getModifiersQuerying().getVitality(game, physicalCard);
                                    }

                                    return vitality;
                                });
                    }
                };
            } else if (type.equalsIgnoreCase("printedStrengthFromMemory")) {
                FieldUtils.validateAllowedFields(object, "memory");
                final String memory = FieldUtils.getString(object.get("memory"), "memory");

                return actionContext -> (Evaluator) (game, cardAffected) -> {
                    int result = 0;
                    for (PhysicalCard physicalCard : actionContext.getCardsFromMemory(memory)) {
                        result += physicalCard.getBlueprint().getStrength();
                    }
                    return result;
                };
            } else if (type.equalsIgnoreCase("subtract")) {
                FieldUtils.validateAllowedFields(object, "firstNumber", "secondNumber");
                final ValueSource firstNumber = ValueResolver.resolveEvaluator(object.get("firstNumber"), 0, environment);
                final ValueSource secondNumber = ValueResolver.resolveEvaluator(object.get("secondNumber"), 0, environment);
                return actionContext -> (Evaluator) (game, cardAffected) -> {
                    final int first = firstNumber.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
                    final int second = secondNumber.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
                    return first - second;
                };
            } else if (type.equals("twilightCostInMemory")) {
                FieldUtils.validateAllowedFields(object, "memory");
                final String memory = FieldUtils.getString(object.get("memory"), "memory");
                return actionContext -> (Evaluator) (game, cardAffected) -> {
                    int total = 0;
                    for (PhysicalCard physicalCard : actionContext.getCardsFromMemory(memory)) {
                        total += physicalCard.getBlueprint().getTwilightCost();
                    }
                    return total;
                };
            } else if (type.equals("maxOfRaces")) {
                FieldUtils.validateAllowedFields(object, "filter");
                final String filter = FieldUtils.getString(object.get("filter"), "filter");

                final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

                return actionContext -> (game, cardAffected) -> {
                    int result = 0;
                    final Filterable filterable = filterableSource.getFilterable(actionContext);
                    for (Race race : Race.values())
                        result = Math.max(result, Filters.countSpottable(game, race, filterable));

                    return result;
                };
            }
            throw new InvalidCardDefinitionException("Unrecognized type of an evaluator " + type);
        }
        throw new InvalidCardDefinitionException("Unable to resolve an evaluator");
    }
}
