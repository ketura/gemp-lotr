package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacksStorage {
    private Map<String, PackBox> _boosterBoxes = new HashMap<String, PackBox>();

    public void addPackBox(String packId, PackBox boosterBox) {
        _boosterBoxes.put(packId, boosterBox);
    }

    public List<CardCollection.Item> openPack(String packId) {
        PackBox boosterBox = _boosterBoxes.get(packId);
        if (boosterBox == null)
            return null;
        return boosterBox.openPack();
    }
}
