package com.gempukku.lotro.game;

import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.util.MultipleComparator;

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
        if (sort == null || sort.equals(""))
            sort = "name";

        final String[] sortSplit = sort.split(",");

        MultipleComparator<CardItem> comparators = new MultipleComparator<CardItem>();
        for (String oneSort : sortSplit) {
            if (oneSort.equals("twilight"))
                comparators.addComparator(new PacksFirstComparator(new TwilightComparator(library)));
            else if (oneSort.equals("siteNumber"))
                comparators.addComparator(new PacksFirstComparator(new SiteNumberComparator(library)));
            else if (oneSort.equals("strength"))
                comparators.addComparator(new PacksFirstComparator(new StrengthComparator(library)));
            else if (oneSort.equals("vitality"))
                comparators.addComparator(new PacksFirstComparator(new VitalityComparator(library)));
            else if (oneSort.equals("collectorInfo"))
                comparators.addComparator(new PacksFirstComparator(new CardBlueprintIdComparator()));
            else if (oneSort.equals("cardType"))
                comparators.addComparator(new PacksFirstComparator(new CardTypeComparator(library)));
            else if (oneSort.equals("culture"))
                comparators.addComparator(new PacksFirstComparator(new CultureComparator(library)));
            else if (oneSort.equals("name"))
                comparators.addComparator(new PacksFirstComparator(new NameComparator(library)));
        }

        Collections.sort(result, comparators);

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
            if (blueprint == null || !GameUtils.getFullName(blueprint).toLowerCase().contains(word.toLowerCase()))
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

    private static class PacksFirstComparator implements Comparator<CardItem> {
        private Comparator<CardItem> _cardComparator;

        private PacksFirstComparator(Comparator<CardItem> cardComparator) {
            _cardComparator = cardComparator;
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            final boolean pack1 = isPack(o1.getBlueprintId());
            final boolean pack2 = isPack(o2.getBlueprintId());
            if (pack1 && pack2)
                return o1.getBlueprintId().compareTo(o2.getBlueprintId());
            else if (pack1)
                return -1;
            else if (pack2)
                return 1;
            else
                return _cardComparator.compare(o1, o2);
        }
    }

    private static class SiteNumberComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;

        private SiteNumberComparator(LotroCardBlueprintLibrary library) {
            _library = library;
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
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

            return siteNumberOne - siteNumberTwo;
        }
    }

    private static class NameComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;

        private NameComparator(LotroCardBlueprintLibrary library) {
            _library = library;
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            return GameUtils.getFullName(_library.getLotroCardBlueprint(o1.getBlueprintId())).compareTo(GameUtils.getFullName(_library.getLotroCardBlueprint(o2.getBlueprintId())));
        }
    }

    private static class TwilightComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;

        private TwilightComparator(LotroCardBlueprintLibrary library) {
            _library = library;
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            return _library.getLotroCardBlueprint(o1.getBlueprintId()).getTwilightCost() - _library.getLotroCardBlueprint(o2.getBlueprintId()).getTwilightCost();
        }
    }

    private static class StrengthComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;

        private StrengthComparator(LotroCardBlueprintLibrary library) {
            _library = library;
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            return getStrengthSafely(_library.getLotroCardBlueprint(o1.getBlueprintId())) - getStrengthSafely(_library.getLotroCardBlueprint(o2.getBlueprintId()));
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

        private VitalityComparator(LotroCardBlueprintLibrary library) {
            _library = library;
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            return getVitalitySafely(_library.getLotroCardBlueprint(o1.getBlueprintId())) - getVitalitySafely(_library.getLotroCardBlueprint(o2.getBlueprintId()));
        }

        private int getVitalitySafely(LotroCardBlueprint blueprint) {
            try {
                return blueprint.getVitality();
            } catch (UnsupportedOperationException exp) {
                return Integer.MAX_VALUE;
            }
        }
    }

    private static class CardTypeComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;

        private CardTypeComparator(LotroCardBlueprintLibrary library) {
            _library = library;
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            CardType cardType1 = _library.getLotroCardBlueprint(o1.getBlueprintId()).getCardType();
            CardType cardType2 = _library.getLotroCardBlueprint(o2.getBlueprintId()).getCardType();
            return cardType1.ordinal() - cardType2.ordinal();
        }
    }

    private static class CultureComparator implements Comparator<CardItem> {
        private LotroCardBlueprintLibrary _library;

        private CultureComparator(LotroCardBlueprintLibrary library) {
            _library = library;
        }

        @Override
        public int compare(CardItem o1, CardItem o2) {
            Culture culture1 = _library.getLotroCardBlueprint(o1.getBlueprintId()).getCulture();
            Culture culture2 = _library.getLotroCardBlueprint(o2.getBlueprintId()).getCulture();

            return getOrdinal(culture1) - getOrdinal(culture2);
        }

        private int getOrdinal(Culture culture) {
            if (culture == null)
                return -1;
            return culture.ordinal();
        }
    }

    private static class CardBlueprintIdComparator implements Comparator<CardItem> {
        @Override
        public int compare(CardItem o1, CardItem o2) {
            String[] o1Parts = o1.getBlueprintId().split("_");
            String[] o2Parts = o2.getBlueprintId().split("_");

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
