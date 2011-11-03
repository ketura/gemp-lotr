package com.gempukku.lotro.game;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;

import java.util.*;

public class DefaultCardCollection implements MutableCardCollection {
    private Map<String, Integer> _counts = new TreeMap<String, Integer>(new CardBlueprintIdComparator());
    private Map<String, LotroCardBlueprint> _cards = new TreeMap<String, LotroCardBlueprint>();
    private LotroCardBlueprintLibrary _library;
    private static final Comparator<Item> NAME_COMPARATOR = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            if (o1.getType() == o2.getType()) {
                if (o1.getType() == Item.Type.PACK)
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else
                    return o1.getCardBlueprint().getName().compareTo(o2.getCardBlueprint().getName());
            } else {
                if (o1.getType() == Item.Type.PACK)
                    return -1;
                else
                    return 1;
            }
        }
    };

    private static final Comparator<Item> TWILIGHT_COMPARATOR = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            if (o1.getType() == o2.getType()) {
                if (o1.getType() == Item.Type.PACK)
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    int twilightResult = o1.getCardBlueprint().getTwilightCost() - o2.getCardBlueprint().getTwilightCost();
                    if (twilightResult == 0)
                        return NAME_COMPARATOR.compare(o1, o2);
                    return twilightResult;
                }
            } else {
                if (o1.getType() == Item.Type.PACK)
                    return -1;
                else
                    return 1;
            }
        }
    };

    private static final Comparator<Item> STRENGTH_COMPARATOR = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            if (o1.getType() == o2.getType()) {
                if (o1.getType() == Item.Type.PACK)
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    int strengthResult = getStrengthSafely(o1.getCardBlueprint()) - getStrengthSafely(o2.getCardBlueprint());
                    if (strengthResult == 0)
                        return NAME_COMPARATOR.compare(o1, o2);
                    return strengthResult;
                }
            } else {
                if (o1.getType() == Item.Type.PACK)
                    return -1;
                else
                    return 1;
            }
        }

        private int getStrengthSafely(LotroCardBlueprint blueprint) {
            try {
                return blueprint.getStrength();
            } catch (UnsupportedOperationException exp) {
                return Integer.MAX_VALUE;
            }
        }
    };

    private static final Comparator<Item> VITALITY_COMPARATOR = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            if (o1.getType() == o2.getType()) {
                if (o1.getType() == Item.Type.PACK)
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    int strengthResult = getVitalitySafely(o1.getCardBlueprint()) - getVitalitySafely(o2.getCardBlueprint());
                    if (strengthResult == 0)
                        return NAME_COMPARATOR.compare(o1, o2);
                    return strengthResult;
                }
            } else {
                if (o1.getType() == Item.Type.PACK)
                    return -1;
                else
                    return 1;
            }
        }

        private int getVitalitySafely(LotroCardBlueprint blueprint) {
            try {
                return blueprint.getVitality();
            } catch (UnsupportedOperationException exp) {
                return Integer.MAX_VALUE;
            }
        }
    };

    public DefaultCardCollection(LotroCardBlueprintLibrary library) {
        _library = library;
    }

    @Override
    public void addCards(String blueprintId, LotroCardBlueprint blueprint, int count) {
        if (blueprint == null)
            throw new IllegalArgumentException("blueprint == null");
        _counts.put(blueprintId, count);
        _cards.put(blueprintId, blueprint);
    }

    @Override
    public void addPacks(String packId, int count) {
        _counts.put(packId, count);
    }

    @Override
    public Map<String, Integer> getAll() {
        return Collections.unmodifiableMap(_counts);
    }

    @Override
    public List<Item> getItems(String filter) {
        if (filter == null)
            filter = "";
        String[] filterParams = filter.split(" ");

        String setStr = getSetNumber(filterParams);
        String[] sets = null;
        if (setStr != null)
            sets = setStr.split(",");
        List<String> words = getWords(filterParams);
        List<CardType> cardTypes = getEnumFilter(CardType.values(), CardType.class, "cardType", null, filterParams);
        List<Culture> cultures = getEnumFilter(Culture.values(), Culture.class, "culture", null, filterParams);
        List<Keyword> keywords = getEnumFilter(Keyword.values(), Keyword.class, "keyword", Collections.<Keyword>emptyList(), filterParams);
        Integer siteNumber = getSiteNumber(filterParams);

        List<Item> result = new ArrayList<Item>();

        for (Map.Entry<String, Integer> itemCount : _counts.entrySet()) {
            String blueprintId = itemCount.getKey();
            int count = itemCount.getValue();

            final LotroCardBlueprint blueprint = _cards.get(blueprintId);
            if (blueprint == null)
                result.add(new Item(Item.Type.PACK, count, blueprintId));
            else {
                if (sets == null || isInSets(blueprintId, sets))
                    if (cardTypes == null || cardTypes.contains(blueprint.getCardType()))
                        if (cultures == null || cultures.contains(blueprint.getCulture()))
                            if (containsAllKeywords(blueprint, keywords))
                                if (containsAllWords(blueprint, words))
                                    if (siteNumber == null || blueprint.getSiteNumber() == siteNumber)
                                        result.add(new Item(Item.Type.CARD, count, blueprintId, blueprint));
            }
        }
        String sort = getSort(filterParams);
        if (sort != null && sort.equals("twilight"))
            Collections.sort(result, TWILIGHT_COMPARATOR);
        else if (sort != null && sort.equals("strength"))
            Collections.sort(result, STRENGTH_COMPARATOR);
        else if (sort != null && sort.equals("vitality"))
            Collections.sort(result, VITALITY_COMPARATOR);
        else
            Collections.sort(result, NAME_COMPARATOR);

        return result;
    }

    private boolean isInSets(String blueprintId, String[] sets) {
        for (String set : sets)
            if (blueprintId.startsWith(set + "_") || _library.hasAlternateInSet(blueprintId, Integer.parseInt(set)))
                return true;

        return false;
    }

    private String getSetNumber(String[] filterParams) {
        for (String filterParam : filterParams) {
            if (filterParam.startsWith("set:"))
                return filterParam.substring("set:".length());
        }
        return null;
    }

    private List<String> getWords(String[] filterParams) {
        List<String> result = new LinkedList<String>();
        for (String filterParam : filterParams) {
            if (filterParam.startsWith("name:"))
                result.add(filterParam.substring("name:".length()));
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

    private String getSort(String[] filterParams) {
        for (String filterParam : filterParams) {
            if (filterParam.startsWith("sort:"))
                return filterParam.substring("sort:".length());
        }
        return null;
    }

    private boolean containsAllKeywords(LotroCardBlueprint blueprint, List<Keyword> keywords) {
        for (Keyword keyword : keywords) {
            if (blueprint == null || !blueprint.hasKeyword(keyword))
                return false;
        }
        return true;
    }

    private boolean containsAllWords(LotroCardBlueprint blueprint, List<String> words) {
        for (String word : words) {
            if (blueprint == null || !blueprint.getName().toLowerCase().contains(word.toLowerCase()))
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
            if (o1Parts[0].equals(o2Parts[0])) {
                final int firstNumber = getAvailableNumber(o1Parts[1]);
                final int secondNumber = getAvailableNumber(o2Parts[1]);

                if (firstNumber != secondNumber)
                    return firstNumber - secondNumber;

                return compareSameCardNumber(o1Parts[1], o2Parts[1]);
            } else {
                return getAvailableNumber(o1Parts[0]) - getAvailableNumber(o2Parts[0]);
            }
        }

        private int compareSameCardNumber(String card1, String card2) {
            if (card1.endsWith("*") && card2.endsWith("*"))
                return compareNotFoils(card1.substring(0, card1.length() - 1), card2.substring(0, card2.length() - 1));
            if (card1.endsWith("*"))
                return -1;
            if (card2.endsWith("*"))
                return 1;
            return compareNotFoils(card1, card2);
        }

        private int compareNotFoils(String card1, String card2) {
            return card1.compareTo(card2);
        }

        private int getAvailableNumber(String str) {
            return Integer.parseInt(str.substring(0, getFirstNonDigitIndex(str)));
        }

        private int getFirstNonDigitIndex(String str) {
            final char[] chars = str.toCharArray();
            for (int i = 0; i < chars.length; i++)
                if (!Character.isDigit(chars[i]))
                    return i;
            return chars.length;
        }
    }
}
