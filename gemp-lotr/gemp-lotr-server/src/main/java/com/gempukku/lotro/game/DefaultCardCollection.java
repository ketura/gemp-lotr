package com.gempukku.lotro.game;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;

import java.util.*;

public class DefaultCardCollection implements CardCollection {
    private Map<String, Integer> _cardsCount = new TreeMap<String, Integer>();
    private Map<String, LotroCardBlueprint> _cards = new TreeMap<String, LotroCardBlueprint>(new CardBlueprintIdComparator());

    public void addCards(String blueprintId, LotroCardBlueprint blueprint, int count) {
        _cardsCount.put(blueprintId, count);
        _cards.put(blueprintId, blueprint);
    }

    @Override
    public Map<String, Integer> filter(String filter) {
        if (filter == null)
            filter = "";
        String[] filterParams = filter.split(" ");

        List<CardType> cardTypes = getEnumFilter(CardType.values(), CardType.class, "cardType", Arrays.asList(CardType.values()), filterParams);
        List<Keyword> keywords = getEnumFilter(Keyword.values(), Keyword.class, "keyword", Collections.<Keyword>emptyList(), filterParams);
        Integer siteNumber = getSiteNumber(filterParams);

        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, LotroCardBlueprint> stringLotroCardBlueprintEntry : _cards.entrySet()) {
            String blueprintId = stringLotroCardBlueprintEntry.getKey();
            LotroCardBlueprint blueprint = stringLotroCardBlueprintEntry.getValue();
            if (cardTypes.contains(blueprint.getCardType()))
                if (containsAllKeywords(blueprint, keywords))
                    if (siteNumber == null || blueprint.getSiteNumber() == siteNumber.intValue())
                        result.put(blueprintId, _cardsCount.get(blueprintId));
        }
        return result;
    }

    private Integer getSiteNumber(String[] filterParams) {
        for (String filterParam : filterParams) {
            if (filterParam.startsWith("siteNumber:"))
                return Integer.parseInt(filterParam.substring("siteNumber:".length()));
        }
        return null;
    }

    private boolean containsAllKeywords(LotroCardBlueprint blueprint, List<Keyword> keywords) {
        for (Keyword keyword : keywords) {
            if (!blueprint.hasKeyword(keyword))
                return false;
        }
        return true;
    }

    private <T extends Enum> List<T> getEnumFilter(T[] enumValues, Class<T> enumType, String prefix, List<T> defaultResult, String[] filterParams) {
        for (String filterParam : filterParams) {
            if (filterParam.startsWith(prefix + ":")) {
                String values = filterParam.substring((prefix + ":").length());
                if (values.startsWith("-")) {
                    values = values.substring(1);
                    List<T> cardTypes = new LinkedList<T>(Arrays.asList(enumValues));
                    for (String v : values.split(",")) {
                        T t = (T) Enum.valueOf(enumType, v);
                        if (t != null)
                            cardTypes.remove((T) t);
                    }
                    return cardTypes;
                } else {
                    List<T> cardTypes = new LinkedList<T>();
                    for (String v : values.split(","))
                        cardTypes.add((T) Enum.valueOf(enumType, v));
                    return cardTypes;
                }
            }
        }
        return defaultResult;
    }

    private static class CardBlueprintIdComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            String[] o1Parts = o1.split("_");
            String[] o2Parts = o2.split("_");
            if (o1Parts[0].equals(o2Parts[0]))
                return Integer.parseInt(o1Parts[1]) - Integer.parseInt(o2Parts[1]);
            else
                return Integer.parseInt(o1Parts[0]) - Integer.parseInt(o2Parts[0]);
        }
    }
}
