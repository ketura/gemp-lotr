package com.gempukku.lotro.packs;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.game.CardCollection;

import java.util.*;

public class RarityPackBox implements PackBox {
    private Random _random = new Random();
    private SetRarity _setRarity;

    public RarityPackBox(int setNo) {
        RarityReader rarityReader = new RarityReader();
        _setRarity = rarityReader.getSetRarity(String.valueOf(setNo));
    }

    @Override
    public List<CardCollection.Item> openPack() {
        List<CardCollection.Item> result = new LinkedList<CardCollection.Item>();
        boolean hasFoil = (_random.nextInt(6) == 0);
        if (hasFoil) {
            int foilRarity = _random.nextInt(11);
            if (foilRarity == 0) {
                List<String> possibleCards = new LinkedList<String>();
                possibleCards.addAll(_setRarity.getCardsOfRarity("R"));
                possibleCards.addAll(_setRarity.getCardsOfRarity("P"));
                possibleCards.addAll(_setRarity.getCardsOfRarity("A"));
                Collections.shuffle(possibleCards, _random);
                addCards(result, possibleCards.subList(0, 1), true);
            } else if (foilRarity < 4) {
                List<String> possibleCards = new LinkedList<String>(_setRarity.getCardsOfRarity("U"));
                Collections.shuffle(possibleCards, _random);
                addCards(result, possibleCards.subList(0, 1), true);
            } else {
                List<String> possibleCards = new LinkedList<String>(_setRarity.getCardsOfRarity("C"));
                Collections.shuffle(possibleCards, _random);
                addCards(result, possibleCards.subList(0, 1), true);
            }
        }
        addRandomRareCard(result, 1);
        addRandomCardsOfRarity(result, 3, "U");
        addRandomCardsOfRarity(result, hasFoil ? 6 : 7, "C");
        return result;
    }

    private void addRandomRareCard(List<CardCollection.Item> result, int count) {
        List<String> possibleCards = new LinkedList<String>(_setRarity.getCardsOfRarity("R"));
        possibleCards.addAll(_setRarity.getCardsOfRarity("R"));
        possibleCards.addAll(_setRarity.getCardsOfRarity("R"));
        possibleCards.addAll(_setRarity.getCardsOfRarity("A"));
        Collections.shuffle(possibleCards, _random);
        addCards(result, possibleCards.subList(0, count), false);
    }

    private void addRandomCardsOfRarity(List<CardCollection.Item> result, int count, String rarity) {
        List<String> possibleCards = new LinkedList<String>(_setRarity.getCardsOfRarity(rarity));
        Collections.shuffle(possibleCards, _random);
        addCards(result, possibleCards.subList(0, count), false);
    }

    private void addCards(List<CardCollection.Item> result, Collection<String> cards, boolean foil) {
        for (String card : cards) {
            card = card.replace("P", "_");
            card = card.replace("A", "_");
            card = card.replace("R", "_");
            card = card.replace("U", "_");
            card = card.replace("C", "_");
            result.add(new CardCollection.Item(CardCollection.Item.Type.CARD, 1, card + (foil ? "*" : "")));
        }
    }
}
