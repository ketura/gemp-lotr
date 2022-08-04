package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.CardSets;
import com.gempukku.lotro.game.packs.SetDefinition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomFoilPack implements PackBox {
    private final List<String> _availableCards = new ArrayList<>();

    public RandomFoilPack(String rarity, String[] sets, CardSets cardSets) {
        for (SetDefinition setDefinition : cardSets.getSetDefinitions().values()) {
            _availableCards.addAll(setDefinition.getCardsOfRarity(rarity));
        }
    }

    @Override
    public List<CardCollection.Item> openPack() {
        List<CardCollection.Item> result = new LinkedList<>();
        final String cardBlueprintId = _availableCards.get(ThreadLocalRandom.current().nextInt(_availableCards.size())) + "*";
        result.add(CardCollection.Item.createItem(cardBlueprintId, 1));
        return result;
    }
}
