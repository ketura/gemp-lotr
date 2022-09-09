package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.packs.SetDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomFoilPack implements PackBox {
    private final List<String> _availableCards = new ArrayList<>();

    public RandomFoilPack(String rarity, String[] sets, LotroCardBlueprintLibrary library) {
        for (SetDefinition setDefinition : library.getSetDefinitions().values()) {
            _availableCards.addAll(setDefinition.getCardsOfRarity(rarity));
        }
    }

    @Override
    public List<CardCollection.Item> openPack() {
        return openPack(ThreadLocalRandom.current().nextInt(_availableCards.size()));
    }

    @Override
    public List<CardCollection.Item> openPack(int selection) {
        final String bpID = _availableCards.stream().skip(selection).findFirst().get() + "*";
        return Collections.singletonList(CardCollection.Item.createItem(bpID, 1));
    }

    @Override
    public List<String> GetAllOptions() {
        return Collections.unmodifiableList(_availableCards);
    }
}
