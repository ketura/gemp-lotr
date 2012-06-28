package com.gempukku.lotro.packs;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.game.CardCollection;

import java.util.*;

public class ReflectionsPackBox implements PackBox {
    private Random _random = new Random();
    private SetRarity _reflectionsRarity;
    private List<String> _previousSetCards = new ArrayList<String>();

    public ReflectionsPackBox() {
        RarityReader rarityReader = new RarityReader();
        _reflectionsRarity = rarityReader.getSetRarity("9");

        for (int set = 1; set <= 6; set++) {
            final SetRarity setRarity = rarityReader.getSetRarity(String.valueOf(set));
            _previousSetCards.addAll(setRarity.getCardsOfRarity("R"));
            _previousSetCards.addAll(setRarity.getCardsOfRarity("P"));
            for (int i = 0; i < 3; i++)
                _previousSetCards.addAll(setRarity.getCardsOfRarity("U"));
            for (int i = 0; i < 7; i++)
                _previousSetCards.addAll(setRarity.getCardsOfRarity("C"));
        }
    }

    @Override
    public List<CardCollection.Item> openPack() {
        List<CardCollection.Item> result = new LinkedList<CardCollection.Item>();
        boolean foil;
        foil = _random.nextInt(15) == 0;
        result.add(CardCollection.Item.createItem(getRandomReflectionsCard() + (foil ? "*" : ""), 1));
        foil = _random.nextInt(15) == 0;
        result.add(CardCollection.Item.createItem(getRandomReflectionsCard() + (foil ? "*" : ""), 1));

        for (int i = 0; i < 16; i++) {
            final String blueprintId = _previousSetCards.get(_random.nextInt(_previousSetCards.size()));
            // There is a 1/6 * 1/11 chance it will be a foil
            if (_random.nextInt(66) == 0)
                result.add(CardCollection.Item.createItem(blueprintId + "*", 1));
            else
                result.add(CardCollection.Item.createItem(blueprintId, 1));
        }

        return result;
    }

    public String getRandomReflectionsCard() {
        List<String> possibleCards = new ArrayList<String>();
        possibleCards.addAll(_reflectionsRarity.getCardsOfRarity("R"));
        possibleCards.addAll(_reflectionsRarity.getCardsOfRarity("R"));
        possibleCards.addAll(_reflectionsRarity.getCardsOfRarity("R"));
        possibleCards.addAll(_reflectionsRarity.getCardsOfRarity("R"));
        possibleCards.addAll(_reflectionsRarity.getCardsOfRarity("X"));
        Collections.shuffle(possibleCards, _random);
        return possibleCards.get(0);
    }
}
