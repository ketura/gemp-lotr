package com.gempukku.lotro.packs;

import com.gempukku.lotro.cards.CardSets;
import com.gempukku.lotro.cards.packs.SetDefinition;
import com.gempukku.lotro.game.CardCollection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomFoilPack implements PackBox {
    private List<String> _availableCards = new ArrayList<String>();

    public RandomFoilPack(String rarity, String[] sets, CardSets cardSets) {
        for (SetDefinition setDefinition : cardSets.getSetDefinitions().values()) {
            _availableCards.addAll(setDefinition.getCardsOfRarity(rarity));
        }
    }

    @Override
    public List<CardCollection.Item> openPack() {
        List<CardCollection.Item> result = new LinkedList<CardCollection.Item>();
        final String cardBlueprintId = _availableCards.get(new Random().nextInt(_availableCards.size())) + "*";
        result.add(CardCollection.Item.createItem(cardBlueprintId, 1));
        return result;
    }
}
