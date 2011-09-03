package com.gempukku.lotro.game;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;

import java.util.*;

public class DefaultCardCollection implements CardCollection {
    private Map<String, Integer> _cardsCount = new TreeMap<String, Integer>();
    private Map<String, LotroCardBlueprint> _cards = new TreeMap<String, LotroCardBlueprint>();

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

        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, LotroCardBlueprint> stringLotroCardBlueprintEntry : _cards.entrySet()) {
            String blueprintId = stringLotroCardBlueprintEntry.getKey();
            LotroCardBlueprint blueprint = stringLotroCardBlueprintEntry.getValue();
            if (cardTypes.contains(blueprint.getCardType()))
                if (containsAllKeywords(blueprint, keywords))
                    result.put(blueprintId, _cardsCount.get(blueprintId));
        }
        return result;
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
}
