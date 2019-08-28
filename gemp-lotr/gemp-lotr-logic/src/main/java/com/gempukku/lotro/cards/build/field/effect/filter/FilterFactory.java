package com.gempukku.lotro.cards.build.field.effect.filter;

import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

        simpleFilters.put("ring bearer", (playerId, game, source, effectResult, effect) -> Filters.ringBearer);
        simpleFilters.put("any", (playerId, game, source, effectResult, effect) -> Filters.any);
        simpleFilters.put("self", (playerId, game, source, effectResult, effect) -> source);
        simpleFilters.put("your", (playerId, game, source, effectResult, effect) -> Filters.owner(source.getOwner()));
        simpleFilters.put("weapon", (playerId, game, source, effectResult, effect) -> Filters.weapon);
        simpleFilters.put("skirmishloser",
                (playerId, game, source, effectResult, effect) -> {
                    final CharacterLostSkirmishResult lostSkirmish = (CharacterLostSkirmishResult) effectResult;
                    return lostSkirmish.getLoser();
                });
        simpleFilters.put("unbound",
                (playerId, game, source, effectResult, effect) -> Filters.not(Keyword.RING_BOUND));

        parameterFilters.put("culture", (parameter, filterFactory) -> {
            final Culture culture = Culture.valueOf(parameter.toUpperCase());
            if (culture == null)
                throw new InvalidCardDefinitionException("Unable to find culture for: " + parameter);

            return (playerId, game, source, effectResult, effect) -> culture;
        });
        parameterFilters.put("side", (parameter, filterFactory) -> {
            final Side side = Side.valueOf(parameter.toUpperCase().replace(" ", "_"));
            if (side == null)
                throw new InvalidCardDefinitionException("Unable to find side for: " + parameter);

            return (playerId, game, source, effectResult, effect) -> side;
        });
        parameterFilters.put("hasAttached",
                (parameter, filterFactory) -> {
                    final FilterableSource filterableSource = filterFactory.generateFilter(parameter);
                    return (playerId, game, source, effectResult, effect) -> Filters.hasAttached(filterableSource.getFilterable(playerId, game, source, effectResult, effect));
                });
        parameterFilters.put("hasStacked",
                (parameter, filterFactory) -> {
                    final FilterableSource filterableSource = filterFactory.generateFilter(parameter);
                    return (playerId, game, source, effectResult, effect) -> Filters.hasStacked(filterableSource.getFilterable(playerId, game, source, effectResult, effect));
                });
        parameterFilters.put("attachedTo",
                (parameter, filterFactory) -> {
                    final FilterableSource filterableSource = filterFactory.generateFilter(parameter);
                    return (playerId, game, source, effectResult, effect) -> Filters.attachedTo(filterableSource.getFilterable(playerId, game, source, effectResult, effect));
                });
        parameterFilters.put("name",
                (parameter, filterFactory) -> (playerId, game, source, effectResult, effect) -> Filters.name(parameter));
        parameterFilters.put("inSkirmishAgainst",
                (parameter, filterFactory) -> {
                    final FilterableSource filterableSource = filterFactory.generateFilter(parameter);
                    return (playerId, game, source, effectResult, effect) -> Filters.inSkirmishAgainst(filterableSource.getFilterable(playerId, game, source, effectResult, effect));
                });
        parameterFilters.put("assignedToSkirmish",
                (parameter, filterFactory) -> {
                    final FilterableSource filterableSource = filterFactory.generateFilter(parameter);
                    return (playerId, game, source, effectResult, effect) -> Filters.assignedToSkirmishAgainst(filterableSource.getFilterable(playerId, game, source, effectResult, effect));
                });
        parameterFilters.put("resistanceLessThan",
                (parameter, filterFactory) -> {
                    int amount = Integer.parseInt(parameter);
                    return (playerId, game, source, effectResult, effect) -> Filters.maxResistance(amount + 1);
                });
        parameterFilters.put("not",
                (parameter, filterFactory) -> {
                    final FilterableSource filterableSource = filterFactory.generateFilter(parameter);
                    return (playerId, game, source, effectResult, effect) -> Filters.not(filterableSource.getFilterable(playerId, game, source, effectResult, effect));
                });
        parameterFilters.put("allyHome",
                (parameter, filterFactory) -> {
                    final String[] parameterSplit = parameter.split(",");
                    final SitesBlock sitesBlock = Enum.valueOf(SitesBlock.class, parameterSplit[0].toUpperCase().replace('_', ' '));
                    int number = Integer.parseInt(parameterSplit[1]);
                    return (playerId, game, source, effectResult, effect) -> Filters.isAllyHome(number, sitesBlock);
                });
        parameterFilters.put("siteBlock",
                (parameter, filterFactory) -> {
                    final SitesBlock sitesBlock = Enum.valueOf(SitesBlock.class, parameter.toUpperCase().replace(' ', '_'));
                    return (playerId, game, source, effectResult, effect) -> Filters.siteBlock(sitesBlock);
                });
        parameterFilters.put("siteNumber",
                (parameter, filterFactory) -> {
                    int number = Integer.parseInt(parameter);
                    return (playerId, game, source, effectResult, effect) -> Filters.siteNumber(number);
                });
        parameterFilters.put("or",
                (parameter, filterFactory) -> {
                    final String[] filters = splitIntoFilters(parameter);
                    FilterableSource[] filterables = new FilterableSource[filters.length];
                    for (int i = 0; i < filters.length; i++)
                        filterables[i] = filterFactory.generateFilter(filters[i]);
                    return (playerId, game, source, effectResult, effect) -> {
                        Filterable[] filters1 = new Filterable[filterables.length];
                        for (int i = 0; i < filterables.length; i++)
                            filters1[i] = filterables[i].getFilterable(playerId, game, source, effectResult, effect);

                        return Filters.or(filters1);
                    };
                });
    }

    private void appendFilter(Filterable value) {
        final String filterName = value.toString().toLowerCase().replace("_", " ");
        if (simpleFilters.containsKey(filterName))
            throw new RuntimeException("Duplicate filter name: " + filterName);
        simpleFilters.put(filterName, (playerId, game, source, effectResult, effect) -> value);
    }

    public FilterableSource generateFilter(String value) throws InvalidCardDefinitionException {
        String filterStrings[] = splitIntoFilters(value);
        if (filterStrings.length == 0)
            return (playerId, game, source, effectResult, effect) -> Filters.any;
        if (filterStrings.length == 1)
            return createFilter(filterStrings[0]);

        FilterableSource[] filters = new FilterableSource[filterStrings.length];
        for (int i = 0; i < filters.length; i++)
            filters[i] = createFilter(filterStrings[i]);
        return (playerId, game, source, effectResult, effect) -> {
            Filterable[] filter = new Filterable[filters.length];
            for (int i = 0; i < filter.length; i++) {
                filter[i] = filters[i].getFilterable(playerId, game, source, effectResult, effect);
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
            throw new InvalidCardDefinitionException("Invalid filter definition: " + value);

        parts.add(sb.toString());

        return parts.toArray(new String[0]);
    }

    private FilterableSource createFilter(String filterString) throws InvalidCardDefinitionException {
        if (filterString.contains("(") && filterString.endsWith(")")) {
            String filterName = filterString.substring(0, filterString.indexOf("("));
            String filterParameter = filterString.substring(filterString.indexOf("(") + 1, filterString.lastIndexOf(")"));
            return lookupFilter(filterName, filterParameter);
        }
        return lookupFilter(filterString, null);
    }

    private FilterableSource lookupFilter(String name, String parameter) throws InvalidCardDefinitionException {
        if (parameter == null) {
            FilterableSource result = simpleFilters.get(name.toLowerCase());
            if (result != null)
                return result;
        }

        final FilterableSourceProducer filterableSourceProducer = parameterFilters.get(name);
        if (filterableSourceProducer == null)
            throw new InvalidCardDefinitionException("Unable to find filter: " + name);

        return filterableSourceProducer.createFilterableSource(parameter, this);
    }
}
