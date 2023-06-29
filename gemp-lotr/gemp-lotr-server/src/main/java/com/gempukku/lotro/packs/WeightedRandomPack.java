package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class WeightedRandomPack implements PackBox {
    private record Reward(String name, int quantity, int weight) { }

    private final Map<String, Reward> _contents = new LinkedHashMap<>();

    private WeightedRandomPack() { }

    public static WeightedRandomPack LoadFromArray(Iterable<String> items) {
        WeightedRandomPack box = new WeightedRandomPack();
        for (String item : items) {
            item = item.trim();
            if (item.length() > 0) {
                String[] result = item.split("[x%]", 3);
                if(result.length != 3) {
                    System.out.println("Unexpected number of entries in a WeightedRandomPack! Skipping: '" + item + "'");
                    continue;
                }
                var reward = new Reward(result[2], Integer.parseInt(result[1]), Integer.parseInt(result[0]));
                box._contents.put(reward.name, reward);
            }
        }

        return box;
    }

    @Override
    public List<CardCollection.Item> openPack() {
        int totalWeight = _contents.values().stream()
                .mapToInt(Reward::weight)
                .sum();

        return openPack(ThreadLocalRandom.current().nextInt(totalWeight) + 1);
    }

    @Override
    public List<CardCollection.Item> openPack(int roll) {
        int currentPercent = 0;

        for (Reward reward : _contents.values()) {
            currentPercent += reward.weight;
            if (roll <= currentPercent) {
                return generateItems(reward.name);
            }
        }

        return generateItems(_contents.keySet().stream().findFirst().get());
    }

    public List<CardCollection.Item> generateItems(String name) {
        var result = CardCollection.Item.createItem(name, _contents.get(name).quantity, true);
        return Collections.singletonList(result);
    }

    @Override
    public List<String> GetAllOptions() {
        return _contents.keySet().stream().toList();
    }
}
