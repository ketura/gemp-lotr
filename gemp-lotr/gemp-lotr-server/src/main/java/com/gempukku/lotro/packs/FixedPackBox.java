package com.gempukku.lotro.packs;

import com.gempukku.lotro.common.AppConfig;
import com.gempukku.lotro.game.CardCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FixedPackBox implements PackBox {
    private final Map<String, Integer> _contents = new LinkedHashMap<>();

    public FixedPackBox(String packName) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(AppConfig.getResourceStream("product/" + packName + ".pack")))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("#") && line.length() > 0) {
                    String[] result = line.split("x", 2);
                    _contents.put(result[1], Integer.parseInt(result[0]));
                }
            }
        }

    }

    @Override
    public List<CardCollection.Item> openPack() {
        List<CardCollection.Item> result = new LinkedList<>();
        for (Map.Entry<String, Integer> contentsEntry : _contents.entrySet()) {
            String blueprintId = contentsEntry.getKey();
            result.add(CardCollection.Item.createItem(blueprintId, contentsEntry.getValue()));
        }
        return result;
    }
}
