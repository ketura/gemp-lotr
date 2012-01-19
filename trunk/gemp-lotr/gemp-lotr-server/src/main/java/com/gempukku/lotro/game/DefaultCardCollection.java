package com.gempukku.lotro.game;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.packs.PacksStorage;

import java.util.*;

public class DefaultCardCollection implements MutableCardCollection {
    private Map<String, Integer> _counts = new LinkedHashMap<String, Integer>();

    public DefaultCardCollection() {

    }

    public DefaultCardCollection(CardCollection cardCollection) {
        _counts.putAll(cardCollection.getAll());
    }

    private static class NameComparator implements Comparator<Item> {
        private LotroCardBlueprintLibrary _library;

        private NameComparator(LotroCardBlueprintLibrary library) {
            _library = library;
        }

        @Override
        public int compare(Item o1, Item o2) {
            if (o1.getType() == o2.getType()) {
                if (o1.getType() == Item.Type.PACK)
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else
                    return _library.getLotroCardBlueprint(o1.getBlueprintId()).getName().compareTo(_library.getLotroCardBlueprint(o2.getBlueprintId()).getName());
            } else {
                if (o1.getType() == Item.Type.PACK)
                    return -1;
                else
                    return 1;
            }
        }
    }

    private static class TwilightComparator implements Comparator<Item> {
        private LotroCardBlueprintLibrary _library;
        private NameComparator _nameComparator;

        private TwilightComparator(LotroCardBlueprintLibrary library) {
            _library = library;
            _nameComparator = new NameComparator(library);
        }

        @Override
        public int compare(Item o1, Item o2) {
            if (o1.getType() == o2.getType()) {
                if (o1.getType() == Item.Type.PACK)
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    int twilightResult = _library.getLotroCardBlueprint(o1.getBlueprintId()).getTwilightCost() - _library.getLotroCardBlueprint(o2.getBlueprintId()).getTwilightCost();
                    if (twilightResult == 0)
                        return _nameComparator.compare(o1, o2);
                    return twilightResult;
                }
            } else {
                if (o1.getType() == Item.Type.PACK)
                    return -1;
                else
                    return 1;
            }
        }
    }

    private static class StrengthComparator implements Comparator<Item> {
        private LotroCardBlueprintLibrary _library;
        private NameComparator _nameComparator;

        private StrengthComparator(LotroCardBlueprintLibrary library) {
            _library = library;
            _nameComparator = new NameComparator(library);
        }

        @Override
        public int compare(Item o1, Item o2) {
            if (o1.getType() == o2.getType()) {
                if (o1.getType() == Item.Type.PACK)
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    int strengthResult = getStrengthSafely(_library.getLotroCardBlueprint(o1.getBlueprintId())) - getStrengthSafely(_library.getLotroCardBlueprint(o2.getBlueprintId()));
                    if (strengthResult == 0)
                        return _nameComparator.compare(o1, o2);
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
    }

    private static class VitalityComparator implements Comparator<Item> {
        private LotroCardBlueprintLibrary _library;
        private NameComparator _nameComparator;

        private VitalityComparator(LotroCardBlueprintLibrary library) {
            _library = library;
            _nameComparator = new NameComparator(library);
        }

        @Override
        public int compare(Item o1, Item o2) {
            if (o1.getType() == o2.getType()) {
                if (o1.getType() == Item.Type.PACK)
                    return o1.getBlueprintId().compareTo(o2.getBlueprintId());
                else {
                    int strengthResult = getVitalitySafely(_library.getLotroCardBlueprint(o1.getBlueprintId())) - getVitalitySafely(_library.getLotroCardBlueprint(o2.getBlueprintId()));
                    if (strengthResult == 0)
                        return _nameComparator.compare(o1, o2);
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
    }

    @Override
    public void addItem(String itemId, int count) {
        Integer oldCount = _counts.get(itemId);
        if (oldCount == null)
            oldCount = 0;
        _counts.put(itemId, oldCount + count);
    }

    @Override
    public boolean removeItem(String itemId, int count) {
        Integer oldCount = _counts.get(itemId);
        if (oldCount == null || oldCount < count)
            return false;
        final int newCount = oldCount - count;
        if (newCount == 0)
            _counts.remove(itemId);
        else
            _counts.put(itemId, newCount);
        return true;
    }

    private boolean hasSelection(String packId, String selection, PacksStorage packsStorage) {
        for (Item item : packsStorage.openPack(packId)) {
            if (item.getBlueprintId().equals(selection))
                return true;
        }
        return false;
    }

    @Override
    public synchronized CardCollection openPack(String packId, String selection, PacksStorage packsStorage) {
        Integer count = _counts.get(packId);
        if (count == null)
            return null;
        if (count > 0) {
            List<Item> packContents = null;
            if (selection != null && packId.startsWith("(S)")) {
                if (hasSelection(packId, selection, packsStorage)) {
                    packContents = new LinkedList<Item>();
                    Item.Type type = selection.contains("_") ? Item.Type.CARD : Item.Type.PACK;
                    packContents.add(new Item(type, 1, selection));
                }
            } else {
                packContents = packsStorage.openPack(packId);
            }

            if (packContents == null)
                return null;

            DefaultCardCollection packCollection = new DefaultCardCollection();

            for (Item itemFromPack : packContents) {
                addItem(itemFromPack.getBlueprintId(), itemFromPack.getCount());
                packCollection.addItem(itemFromPack.getBlueprintId(), itemFromPack.getCount());
            }

            if (count == 1)
                _counts.remove(packId);
            else
                _counts.put(packId, count - 1);

            return packCollection;
        }
        return null;
    }

    @Override
    public Map<String, Integer> getAll() {
        return Collections.unmodifiableMap(_counts);
    }

    @Override
    public int getItemCount(String blueprintId) {
        Integer count = _counts.get(blueprintId);
        if (count == null)
            return 0;
        return count;
    }

    @Override
    public List<Item> getItems(String filter, LotroCardBlueprintLibrary library) {
        if (filter == null)
            filter = "";
        String[] filterParams = filter.split(" ");

        String type = getTypeFilter(filterParams);
        String[] sets = getSetFilter(filterParams);
        List<String> words = getWords(filterParams);
        Set<CardType> cardTypes = getEnumFilter(CardType.values(), CardType.class, "cardType", null, filterParams);
        Set<Culture> cultures = getEnumFilter(Culture.values(), Culture.class, "culture", null, filterParams);
        Set<Keyword> keywords = getEnumFilter(Keyword.values(), Keyword.class, "keyword", Collections.<Keyword>emptySet(), filterParams);
        Integer siteNumber = getSiteNumber(filterParams);

        List<Item> result = new ArrayList<Item>();

        for (Map.Entry<String, Integer> itemCount : _counts.entrySet()) {
            String blueprintId = itemCount.getKey();
            int count = itemCount.getValue();

            if (acceptsFilters(library, blueprintId, type, sets, cardTypes, cultures, keywords, words, siteNumber)) {
                if (!blueprintId.contains("_"))
                    result.add(new Item(Item.Type.PACK, count, blueprintId));
                else
                    result.add(new Item(Item.Type.CARD, count, blueprintId));
            }
        }

        String sort = getSort(filterParams);
        if (sort != null && sort.equals("twilight"))
            Collections.sort(result, new TwilightComparator(library));
        else if (sort != null && sort.equals("strength"))
            Collections.sort(result, new StrengthComparator(library));
        else if (sort != null && sort.equals("vitality"))
            Collections.sort(result, new VitalityComparator(library));
        else
            Collections.sort(result, new NameComparator(library));

        return result;
    }

    private boolean acceptsFilters(
            LotroCardBlueprintLibrary library, String blueprintId, String type, String[] sets,
            Set<CardType> cardTypes, Set<Culture> cultures, Set<Keyword> keywords, List<String> words, Integer siteNumber) {
        if (!blueprintId.contains("_")) {
            if (type == null || type.equals("pack"))
                return true;
        } else {
            if (type == null
                    || type.equals("card")
                    || (type.equals("foil") && blueprintId.endsWith("*"))
                    || (type.equals("nonFoil") && !blueprintId.endsWith("*"))) {
                final LotroCardBlueprint blueprint = library.getLotroCardBlueprint(blueprintId);
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

    private String[] getSetFilter(String[] filterParams) {
        String setStr = getSetNumber(filterParams);
        String[] sets = null;
        if (setStr != null)
            sets = setStr.split(",");
        return sets;
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

    private static class CardBlueprintIdComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            String[] o1Parts = o1.split("_");
            String[] o2Parts = o2.split("_");

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
