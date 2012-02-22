package com.gempukku.lotro.packs;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.game.CardCollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomFoilPack implements PackBox {
    private List<String> _availableCards = new ArrayList<String>();

    public RandomFoilPack(String rarity, String[] sets) {
        RarityReader rarityReader = new RarityReader();
        for (String set : sets) {
            final SetRarity setRarity = rarityReader.getSetRarity(set);
            _availableCards.addAll(setRarity.getCardsOfRarity(rarity));
        }
    }

    @Override
    public List<CardCollection.Item> openPack() {
        List<CardCollection.Item> result = new LinkedList<CardCollection.Item>();
        final String cardBlueprintId = _availableCards.get(new Random().nextInt(_availableCards.size())) + "*";
        result.add(
                new CardCollection.Item(CardCollection.Item.Type.CARD, 1, cardBlueprintId));
        return result;
    }
}
