package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FixedPackBox implements PackBox {
    private LotroCardBlueprintLibrary _library;

    private Map<String, Integer> _contents = new HashMap<String, Integer>();

    public FixedPackBox(LotroCardBlueprintLibrary library, String packName) throws IOException {
        _library = library;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(FixedPackBox.class.getResourceAsStream("/" + packName + ".pack")));
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null)
                if (!line.startsWith("#")) {
                    String[] result = line.split("x", 2);
                    _contents.put(result[1], Integer.parseInt(result[0]));
                }
        } finally {
            bufferedReader.close();
        }

    }

    @Override
    public List<CardCollection.Item> openPack() {
        List<CardCollection.Item> result = new LinkedList<CardCollection.Item>();
        for (Map.Entry<String, Integer> contentsEntry : _contents.entrySet()) {
            String blueprintId = contentsEntry.getKey();
            result.add(new CardCollection.Item(CardCollection.Item.Type.CARD, contentsEntry.getValue(), blueprintId, _library.getLotroCardBlueprint(blueprintId)));
        }
        return result;
    }
}
