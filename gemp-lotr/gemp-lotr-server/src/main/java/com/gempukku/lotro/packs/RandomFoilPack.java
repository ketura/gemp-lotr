package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.cards.CardBlueprintLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomFoilPack implements PackBox {
    private final List<String> _availableCards = new ArrayList<>();

    public RandomFoilPack(String[] rarities, String[] sets, CardBlueprintLibrary library) {
        var setList = Arrays.stream(sets).toList();
        for (var setDefinition : library.getSetDefinitions().values()) {
            if(!setList.contains(setDefinition.getSetId()))
                continue;
            for(String rarity : rarities) {
                _availableCards.addAll(setDefinition.getCardsOfRarity(rarity));
            }
        }
    }

    @Override
    public List<CardCollection.Item> openPack() {
        return openPack(ThreadLocalRandom.current().nextInt(_availableCards.size()));
    }

    @Override
    public List<CardCollection.Item> openPack(int selection) {
        final String bpID = _availableCards.stream().skip(selection).findFirst().get() + "*";
        return Collections.singletonList(CardCollection.Item.createItem(bpID, 1, true));
    }

    @Override
    public List<String> GetAllOptions() {
        return Collections.unmodifiableList(_availableCards);
    }
}
