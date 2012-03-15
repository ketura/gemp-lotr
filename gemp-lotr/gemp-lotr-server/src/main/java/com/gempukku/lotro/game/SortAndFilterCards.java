package com.gempukku.lotro.game;

import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;

import java.util.*;

public class SortAndFilterCards {
    public <T extends CardItem> List<T> process(String filter, Collection<T> items, LotroCardBlueprintLibrary library, Map<String, SetRarity> rarities) {
        if (filter == null)
            filter = "";
        String[] filterParams = filter.split(" ");

        String type = getTypeFilter(filterParams);
        String rarity = getRarityFilter(filterParams);
        String[] sets = getSetFilter(filterParams);
        List<String> words = getWords(filterParams);
        Set<CardType> cardTypes = getEnumFilter(CardType.values(), CardType.class, "cardType", null, filterParams);
        Set<Culture> cultures = getEnumFilter(Culture.values(), Culture.class, "culture", null, filterParams);
        Set<Keyword> keywords = getEnumFilter(Keyword.values(), Keyword.class, "keyword", Collections.<Keyword>emptySet(), filterParams);
        Integer siteNumber = getSiteNumber(filterParams);

        List<T> result = new ArrayList<T>();

        for (T item : items) {
            String blueprintId = item.getBlueprintId();

            if (acceptsFilters(library, rarities, blueprintId, type, rarity, sets, cardTypes, cultures, keywords, words, siteNumber))
                result.add(item);
        }

        String sort = getSort(filterParams);
        if (sort != null && sort.equals("twilight"))
            Collections.sort(result, new TwilightComparator(library));
        else if (sort != null && sort.equals("siteNumber"))
            Collections.sort(result, new SiteNumberComparator(library));
        else if (sort != null && sort.equals("strength"))
            Collections.sort(result, new StrengthComparator(library));
        else if (sort != null && sort.equals("vitality"))
            Collections.sort(result, new VitalityComparator(library));
        else if (sort != null && sort.equals("collectorInfo"))
            Collections.sort(result, new CardBlueprintIdComparator());
        else
            Collections.sort(result, new NameComparator(library));

        return result;
    }

    private boolean acceptsFilters(
            LotroCardBlueprintLibrary library, Map<String, SetRarity> rarities, String blueprintId, String type, String rarity, String[] sets,
            Set<CardType> cardTypes, Set<Culture> cultures, Set<Keyword> keywords, List<String> words, Integer siteNumber) {
        if (isPack(blueprintId)) {
            if (type == null || type.equals("pack"))
                return true;
        } else {
            if (type == null
                    || type.equals("card")
                    || (type.equals("foil") && blueprintId.endsWith("*"))
                    || (type.equals("nonFoil") && !blueprintId.endsWith("*"))
                    || (type.equals("tengwar") && (blueprintId.endsWith("T*") || blueprintId.endsWith("T")))) {
                final LotroCardBlueprint blueprint = library.getLotroCardBlueprint(blueprintId);
                if (rarity == null || isRarity(blueprintId, rarity, library, rarities))
                    if (sets == null || isInSets(blueprintId, sets, library))
                        if (cardTypes == null || cardTypes.contains(blueprint.getCardType()))
                            if (cultures == null || cultures.contains(blueprint.getCulture()))
                                if (containsAllKeywords(blueprint, keywords))
                                    if (containsAllWords(blueprint, words))
                                        if (siteNumber == null || blueprint.getSiteNumber() == siteNumber)
                                            return true;
            }
        }
        return false;
    }

    private String getTypeFilter(String[] filterParams) {
        for (String filterParam : filterParams) {
            if (filterParam.startsWith("type:"))
                return filterParam.substring("type:".length());
        }
        return null;
    }

    private String getRarityFilter(String[] filterParams) {
        for (String filterParam : filterParams) {
            if (filterParam.startsWith("rarity:"))
                return filterParam.substring("rarity:".length());
        }
        return null;
    }

    private String[] getSetFilter(String[] filterParams) {
        String setStr = getSetNumber(filterParams);
        String[] sets = null;
        if (setStr != null)
            sets = setStr.split(",");
        return sets;
    }

    private boolean isRarity(String blueprintId, String rarity, LotroCardBlueprintLibrary library, Map<String, SetRarity> rarities) {
        if (blueprintId.contains("_")) {
            SetRarity setRarity = rarities.get(blueprintId.substring(0, blueprintId.indexOf("_")));
            if (setRarity != null && setRarity.getCardRarity(library.stripBlueprintModifiers(blueprintId)).equals(rarity))
                return true;
            return false;
        }
        return true;
    }

    private boolean isInSets(String blueprintId, String[] sets, LotroCardBlueprintLibrary library) {
        for (String set : sets)
            if (blueprintId.startsWith(set + "_") || library.hasAlternateInSet(blueprintId, Integer.parseInt(set)))
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

    private boolean containsAllKeywords(LotroCardBlueprint blueprint, Set<Keyword> keywords) {
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

    private <T extends Enum> Set<T> getEnumFilter(T[] enumValues, Class<T> enumType, String prefix, Set<T> defaultResult, String[] filterParams) {
        for (String filterParam : filterParams) {
            if (filterParam.startsWith(prefix + ":")) {
                String values = filterParam.substring((prefix + ":").length());
                if (values.startsWith("-")) {
                    values = values.substring(1);
                    Set<T> cardTypes = new HashSet<T>(Arrays.asList(enumValues));
                    for (String v : values.split(",")) {
                        T t = (T) Enum.valueOf(enumType, v);
                        if (t != null)
                            cardTypes.remove((T) t);
                    }
                    return cardTypes;
                } else {
                    Set<T> cardTypes = new HashSet<T>();
                    for (String v : values.split(","))
                        cardTypes.add((T) Enum.valueOf(enumType, v));
                    return cardTypes;
                }
            }
        }
        return defaultResult;
    }

    private static boolean isPack(String blueprintId) {
        return !blueprintId.contains("_");
    }

    private static class SiteNumberComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;
        private NameComparator _nameComparator;

        private SiteNumberComparator(LotroCardBlueprintLibrary library) {
            _library = library;
            _nameComparator = new NameComparator(library);
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            if (isPack(o1.getBlueprintId()) == isPack(o2.getBlueprintId())) {
                if (isPack(o1.getBlueprintId()))
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    int siteNumberOne;
                    try {
                        siteNumberOne = _library.getLotroCardBlueprint(o1.getBlueprintId()).getSiteNumber();
                    } catch (UnsupportedOperationException exp) {
                        siteNumberOne = 10;
                    }
                    int siteNumberTwo;
                    try {
                        siteNumberTwo = _library.getLotroCardBlueprint(o2.getBlueprintId()).getSiteNumber();
                    } catch (UnsupportedOperationException exp) {
                        siteNumberTwo = 10;
                    }

                    int result = siteNumberOne - siteNumberTwo;
                    if (result == 0)
                        return _nameComparator.compare(o1, o2);
                    return result;
                }
            } else {
                if (isPack(o1.getBlueprintId()))
                    return -1;
                else
                    return 1;
            }
        }
    }

    private static class NameComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;
        private CardBlueprintIdComparator _cardBlueprintIdComparator;

        private NameComparator(LotroCardBlueprintLibrary library) {
            _library = library;
            _cardBlueprintIdComparator = new CardBlueprintIdComparator();
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            if (isPack(o1.getBlueprintId()) == isPack(o2.getBlueprintId())) {
                if (isPack(o1.getBlueprintId()))
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    final int nameCompareResult = _library.getLotroCardBlueprint(o1.getBlueprintId()).getName().compareTo(_library.getLotroCardBlueprint(o2.getBlueprintId()).getName());
                    if (nameCompareResult == 0)
                        return _cardBlueprintIdComparator.compare(o1, o2);
                    return nameCompareResult;
                }
            } else {
                if (isPack(o1.getBlueprintId()))
                    return -1;
                else
                    return 1;
            }
        }
    }

    private static class TwilightComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;
        private NameComparator _nameComparator;

        private TwilightComparator(LotroCardBlueprintLibrary library) {
            _library = library;
            _nameComparator = new NameComparator(library);
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            if (isPack(o1.getBlueprintId()) == isPack(o2.getBlueprintId())) {
                if (isPack(o1.getBlueprintId()))
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    int twilightResult = _library.getLotroCardBlueprint(o1.getBlueprintId()).getTwilightCost() - _library.getLotroCardBlueprint(o2.getBlueprintId()).getTwilightCost();
                    if (twilightResult == 0)
                        return _nameComparator.compare(o1, o2);
                    return twilightResult;
                }
            } else {
                if (isPack(o1.getBlueprintId()))
                    return -1;
                else
                    return 1;
            }
        }
    }

    private static class StrengthComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;
        private NameComparator _nameComparator;

        private StrengthComparator(LotroCardBlueprintLibrary library) {
            _library = library;
            _nameComparator = new NameComparator(library);
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            if (isPack(o1.getBlueprintId()) == isPack(o2.getBlueprintId())) {
                if (isPack(o1.getBlueprintId()))
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    int strengthResult = getStrengthSafely(_library.getLotroCardBlueprint(o1.getBlueprintId())) - getStrengthSafely(_library.getLotroCardBlueprint(o2.getBlueprintId()));
                    if (strengthResult == 0)
                        return _nameComparator.compare(o1, o2);
                    return strengthResult;
                }
            } else {
                if (isPack(o1.getBlueprintId()))
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
    }

    private static class VitalityComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;
        private NameComparator _nameComparator;

        private VitalityComparator(LotroCardBlueprintLibrary library) {
            _library = library;
            _nameComparator = new NameComparator(library);
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            if (isPack(o1.getBlueprintId()) == isPack(o2.getBlueprintId())) {
                if (isPack(o1.getBlueprintId()))
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    int strengthResult = getVitalitySafely(_library.getLotroCardBlueprint(o1.getBlueprintId())) - getVitalitySafely(_library.getLotroCardBlueprint(o2.getBlueprintId()));
                    if (strengthResult == 0)
                        return _nameComparator.compare(o1, o2);
                    return strengthResult;
                }
            } else {
                if (isPack(o1.getBlueprintId()))
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
    }

    private static class CardBlueprintIdComparator implements Comparator<CardItem> {
        @Override
        public int compare(CardItem o1, CardItem o2) {
            if (isPack(o1.getBlueprintId()) && isPack(o2.getBlueprintId()))
                return o1.getBlueprintId().compareTo(o2.getBlueprintId());
            if (isPack(o1.getBlueprintId()))
                return -1;
            if (isPack(o2.getBlueprintId()))
                return 1;
            String[] o1Parts = o1.getBlueprintId().split("_");
            String[] o2Parts = o2.getBlueprintId().split("_");

            if (o1Parts.length == 1 && o2Parts.length == 1)
                return o1Parts[0].compareTo(o2Parts[0]);
            if (o1Parts.length == 1)
                return -1;
            if (o2Parts.length == 1)
                return 1;

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
