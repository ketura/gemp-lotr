package com.gempukku.lotro.cards.build.field.effect.filter;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;

import java.util.*;

public class FilterFactory {
    private Map<String, FilterableSource> simpleFilters = new HashMap<>();
    private Map<String, FilterableSourceProducer> parameterFilters = new HashMap<>();

    public FilterFactory() {
        for (CardType value : CardType.values())
            appendFilter(value);
        for (Keyword value : Keyword.values())
            appendFilter(value);
        for (PossessionClass value : PossessionClass.values())
            appendFilter(value);
        for (Race value : Race.values())
            appendFilter(value);
        for (Signet value : Signet.values())
            appendFilter(value);

        simpleFilters.put("ring bearer", (actionContext) -> Filters.ringBearer);
        simpleFilters.put("any", (actionContext) -> Filters.any);
        simpleFilters.put("self", (actionContext) -> actionContext.getSource());
        simpleFilters.put("your", (actionContext) -> Filters.owner(actionContext.getPerformingPlayer()));
        simpleFilters.put("bearer", (actionContext -> Filters.hasAttached(actionContext.getSource())));
        simpleFilters.put("character", (actionContext) -> Filters.character);
        simpleFilters.put("weapon", (actionContext) -> Filters.weapon);
        simpleFilters.put("wounded", (actionContext) -> Filters.wounded);
        simpleFilters.put("unwounded", (actionContext) -> Filters.unwounded);
        simpleFilters.put("inskirmish", (actionContext) -> Filters.inSkirmish);
        simpleFilters.put("inplay", (actionContext) -> Filters.inPlay);
        simpleFilters.put("notassignedtoskirmish",
                (actionContext) -> Filters.notAssignedToSkirmish);
        simpleFilters.put("skirmishloser",
                (actionContext) -> {
                    final CharacterLostSkirmishResult lostSkirmish = (CharacterLostSkirmishResult) actionContext.getEffectResult();
                    return lostSkirmish.getLoser();
                });
        simpleFilters.put("unbound",
                (actionContext) -> Filters.not(Keyword.RING_BOUND));
        simpleFilters.put("attachedtoinsameregion",
                actionContext -> {
                    final PhysicalCard attachedTo = actionContext.getSource().getAttachedTo();
                    return Filters.region(GameUtils.getRegion(attachedTo.getSiteNumber()));
                });
        simpleFilters.put("currentsite",
                (actionContext) -> Filters.currentSite);

        parameterFilters.put("culture", (parameter, environment) -> {
            final Culture culture = Culture.valueOf(parameter.toUpperCase());
            if (culture == null)
                throw new InvalidCardDefinitionException("Unable to find culture for: " + parameter);

            return (actionContext) -> culture;
        });
        parameterFilters.put("cultureFromMemory", ((parameter, environment) -> actionContext -> {
            Set<Culture> cultures = new HashSet<>();
            for (PhysicalCard physicalCard : actionContext.getCardsFromMemory(parameter)) {
                cultures.add(physicalCard.getBlueprint().getCulture());
            }
            return Filters.or(cultures.toArray(new Culture[0]));
        }));
        parameterFilters.put("side", (parameter, environment) -> {
            final Side side = Side.valueOf(parameter.toUpperCase().replace(" ", "_"));
            if (side == null)
                throw new InvalidCardDefinitionException("Unable to find side for: " + parameter);

            return (actionContext) -> side;
        });
        parameterFilters.put("hasAttached",
                (parameter, environment) -> {
                    final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(parameter, environment);
                    return (actionContext) -> Filters.hasAttached(filterableSource.getFilterable(actionContext));
                });
        parameterFilters.put("hasAttachedCount",
                (parameter, environment) -> {
                    String[] parameterSplit = parameter.split(",", 2);
                    int count = Integer.parseInt(parameterSplit[0]);
                    final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(parameterSplit[1], environment);
                    return (actionContext) -> Filters.hasAttached(count, filterableSource.getFilterable(actionContext));
                });
        parameterFilters.put("hasStacked",
                (parameter, environment) -> {
                    final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(parameter, environment);
                    return (actionContext) -> Filters.hasStacked(filterableSource.getFilterable(actionContext));
                });
        parameterFilters.put("hasStackedCount",
                (parameter, environment) -> {
                    String[] parameterSplit = parameter.split(",", 2);
                    int count = Integer.parseInt(parameterSplit[0]);
                    final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(parameterSplit[1], environment);
                    return (actionContext) -> Filters.hasStacked(count, filterableSource.getFilterable(actionContext));
                });
        parameterFilters.put("attachedTo",
                (parameter, environment) -> {
                    final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(parameter, environment);
                    return (actionContext) -> Filters.attachedTo(filterableSource.getFilterable(actionContext));
                });
        parameterFilters.put("name",
                (parameter, environment) -> (actionContext) -> (Filter)
                        (game, physicalCard) -> physicalCard.getBlueprint().getTitle().equalsIgnoreCase(parameter));
        parameterFilters.put("inSkirmishAgainst",
                (parameter, environment) -> {
                    final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(parameter, environment);
                    return (actionContext) -> Filters.inSkirmishAgainst(filterableSource.getFilterable(actionContext));
                });
        parameterFilters.put("assignedToSkirmish",
                (parameter, environment) -> {
                    final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(parameter, environment);
                    return (actionContext) -> Filters.assignedToSkirmishAgainst(filterableSource.getFilterable(actionContext));
                });
        parameterFilters.put("resistanceLessThan",
                (parameter, environment) -> {
                    int amount = Integer.parseInt(parameter);
                    return (actionContext) -> Filters.maxResistance(amount + 1);
                });
        parameterFilters.put("not",
                (parameter, environment) -> {
                    final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(parameter, environment);
                    return (actionContext) -> Filters.not(filterableSource.getFilterable(actionContext));
                });
        parameterFilters.put("allyHome",
                (parameter, environment) -> {
                    final String[] parameterSplit = parameter.split(",");
                    final SitesBlock sitesBlock = Enum.valueOf(SitesBlock.class, parameterSplit[0].toUpperCase().replace('_', ' '));
                    int number = Integer.parseInt(parameterSplit[1]);
                    return (actionContext) -> Filters.isAllyHome(number, sitesBlock);
                });
        parameterFilters.put("siteBlock",
                (parameter, environment) -> {
                    final SitesBlock sitesBlock = Enum.valueOf(SitesBlock.class, parameter.toUpperCase().replace(' ', '_'));
                    return (actionContext) -> Filters.siteBlock(sitesBlock);
                });
        parameterFilters.put("siteNumber",
                (parameter, environment) -> {
                    int min, max;
                    if (parameter.contains("-")) {
                        final String[] split = parameter.split("-", 2);
                        min = Integer.parseInt(split[0]);
                        max = Integer.parseInt(split[1]);
                    } else {
                        min = max = Integer.parseInt(parameter);
                    }

                    return (actionContext) -> Filters.siteNumberBetweenInclusive(min, max);
                });
        parameterFilters.put("or",
                (parameter, environment) -> {
                    final String[] filters = splitIntoFilters(parameter);
                    FilterableSource[] filterables = new FilterableSource[filters.length];
                    for (int i = 0; i < filters.length; i++)
                        filterables[i] = environment.getFilterFactory().generateFilter(filters[i], environment);
                    return (actionContext) -> {
                        Filterable[] filters1 = new Filterable[filterables.length];
                        for (int i = 0; i < filterables.length; i++)
                            filters1[i] = filterables[i].getFilterable(actionContext);

                        return Filters.or(filters1);
                    };
                });
        parameterFilters.put("memory",
                (parameter, environment) -> (actionContext) -> {
                    return Filters.in(actionContext.getCardsFromMemory(parameter));
                });
        parameterFilters.put("maxTwilight",
                (parameter, environment) -> {
                    if (parameter.startsWith("memory(") && parameter.endsWith(")")) {
                        String memory = parameter.substring(parameter.indexOf("(") + 1, parameter.lastIndexOf(")"));
                        return new FilterableSource() {
                            @Override
                            public Filterable getFilterable(ActionContext actionContext) {
                                final int value = Integer.parseInt(actionContext.getValueFromMemory(memory));
                                return Filters.maxPrintedTwilightCost(value);
                            }
                        };
                    } else {
                        final ValueSource valueSource = ValueResolver.resolveEvaluator(parameter, 0, environment);
                        return new FilterableSource() {
                            @Override
                            public Filterable getFilterable(ActionContext actionContext) {
                                final int value = valueSource.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
                                return Filters.maxPrintedTwilightCost(value);
                            }
                        };
                    }
                });
    }

    private void appendFilter(Filterable value) {
        final String filterName = value.toString().toLowerCase().replace("_", " ");
        final String optionalFilterName = value.toString().toLowerCase().replace("_", "-");
        if (simpleFilters.containsKey(filterName))
            throw new RuntimeException("Duplicate filter name: " + filterName);
        simpleFilters.put(filterName, (actionContext) -> value);
        if (!optionalFilterName.equals(filterName))
            simpleFilters.put(optionalFilterName, (actionContext -> value));
    }

    public FilterableSource generateFilter(String value, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        String filterStrings[] = splitIntoFilters(value);
        if (filterStrings.length == 0)
            return (actionContext) -> Filters.any;
        if (filterStrings.length == 1)
            return createFilter(filterStrings[0], environment);

        FilterableSource[] filters = new FilterableSource[filterStrings.length];
        for (int i = 0; i < filters.length; i++)
            filters[i] = createFilter(filterStrings[i], environment);
        return (actionContext) -> {
            Filterable[] filter = new Filterable[filters.length];
            for (int i = 0; i < filter.length; i++) {
                filter[i] = filters[i].getFilterable(actionContext);
            }

            return Filters.and(filter);
        };
    }

    private String[] splitIntoFilters(String value) throws InvalidCardDefinitionException {
        List<String> parts = new LinkedList<>();
        final char[] chars = value.toCharArray();

        int depth = 0;
        StringBuilder sb = new StringBuilder();
        for (char ch : chars) {
            if (depth > 0) {
                if (ch == ')')
                    depth--;
                if (ch == '(')
                    depth++;
                sb.append(ch);
            } else {
                if (ch == ',') {
                    parts.add(sb.toString());
                    sb = new StringBuilder();
                } else {
                    if (ch == ')')
                        throw new InvalidCardDefinitionException("Invalid filter definition: " + value);
                    if (ch == '(')
                        depth++;
                    sb.append(ch);
                }
            }
        }

        if (depth != 0)
            throw new InvalidCardDefinitionException("Not matching number of opening and closing brackets: " + value);

        parts.add(sb.toString());

        return parts.toArray(new String[0]);
    }

    private FilterableSource createFilter(String filterString, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (filterString.contains("(") && filterString.endsWith(")")) {
            String filterName = filterString.substring(0, filterString.indexOf("("));
            String filterParameter = filterString.substring(filterString.indexOf("(") + 1, filterString.lastIndexOf(")"));
            return lookupFilter(filterName, filterParameter, environment);
        }
        return lookupFilter(filterString, null, environment);
    }

    private FilterableSource lookupFilter(String name, String parameter, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        if (parameter == null) {
            FilterableSource result = simpleFilters.get(name.toLowerCase());
            if (result != null)
                return result;
        }

        final FilterableSourceProducer filterableSourceProducer = parameterFilters.get(name);
        if (filterableSourceProducer == null)
            throw new InvalidCardDefinitionException("Unable to find filter: " + name);

        return filterableSourceProducer.createFilterableSource(parameter, environment);
    }
}
