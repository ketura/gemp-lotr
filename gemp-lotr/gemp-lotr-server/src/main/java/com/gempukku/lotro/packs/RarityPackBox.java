package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.cards.sets.SetDefinition;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RarityPackBox implements PackBox {
    private final SetDefinition _setRarity;
    private final List<String> _possibleRareCards = new ArrayList<>();
    private final List<String> _possibleFoilRareSlot = new ArrayList<>();
    private final List<String> _possibleFoilUncommonSlot = new ArrayList<>();
    private final List<String> _possibleFoilCommonSlot = new ArrayList<>();

    public RarityPackBox(SetDefinition setDefinition) {
        _setRarity = setDefinition;

        _possibleRareCards.addAll(_setRarity.getCardsOfRarity("R"));
        _possibleRareCards.addAll(_setRarity.getCardsOfRarity("R"));
        _possibleRareCards.addAll(_setRarity.getCardsOfRarity("R"));
        _possibleRareCards.addAll(_setRarity.getCardsOfRarity("A"));

        _possibleFoilRareSlot.addAll(_setRarity.getCardsOfRarity("R"));
        _possibleFoilRareSlot.addAll(_setRarity.getCardsOfRarity("P"));
        _possibleFoilRareSlot.addAll(_setRarity.getCardsOfRarity("A"));

        _possibleFoilUncommonSlot.addAll(_setRarity.getCardsOfRarity("U"));
        _possibleFoilUncommonSlot.addAll(_setRarity.getCardsOfRarity("S"));

        _possibleFoilCommonSlot.addAll(_setRarity.getCardsOfRarity("C"));
    }

    @Override
    public List<CardCollection.Item> openPack() {
        List<CardCollection.Item> result = new LinkedList<>();
        boolean hasFoil = (ThreadLocalRandom.current().nextInt(6) == 0);
        if (hasFoil) {
            int foilRarity = ThreadLocalRandom.current().nextInt(11);
            if (foilRarity == 0) {
                addRandomCardsFromList(result, 1, _possibleFoilRareSlot, true);
            } else if (foilRarity < 4) {
                addRandomCardsFromList(result, 1, _possibleFoilUncommonSlot, true);
            } else {
                addRandomCardsFromList(result, 1, _possibleFoilCommonSlot, true);
            }
        }

        addCard(result, _possibleRareCards.get(ThreadLocalRandom.current().nextInt(_possibleRareCards.size())), false);

        addRandomCardsOfRarity(result, 3, "U");
        addRandomCardsOfRarity(result, hasFoil ? 6 : 7, "C");
        return result;
    }

    private void addCard(List<CardCollection.Item> result, String card, boolean foil) {
        result.add(CardCollection.Item.createItem(card + (foil ? "*" : ""), 1));
    }

    private void addRandomCardsOfRarity(List<CardCollection.Item> result, int count, String rarity) {
        final List<String> cardsOfRarity = _setRarity.getCardsOfRarity(rarity);
        addRandomCardsFromList(result, count, cardsOfRarity, false);
    }

    private void addRandomCardsFromList(List<CardCollection.Item> result, int count, List<String> cardList, boolean foil) {
        for (Integer cardIndex : getRandomIndices(count, cardList.size()))
            addCard(result, cardList.get(cardIndex), foil);
    }

    private Set<Integer> getRandomIndices(int count, int elementCount) {
        Set<Integer> addedIndices = new HashSet<>();
        for (int i = 0; i < count; i++) {
            int index;
            do {
                index = ThreadLocalRandom.current().nextInt(elementCount);
            } while (addedIndices.contains(index));
            addedIndices.add(index);
        }
        return addedIndices;
    }

    @Override
    public List<CardCollection.Item> openPack(int selection) { return openPack(); }

    @Override
    public List<String> GetAllOptions() {
        var list = new ArrayList<String>();
        list.addAll(_possibleRareCards);
        list.addAll(_possibleFoilRareSlot);
        list.addAll(_possibleFoilCommonSlot);
        list.addAll(_possibleFoilUncommonSlot);
        return Collections.unmodifiableList(list);
    }
}
