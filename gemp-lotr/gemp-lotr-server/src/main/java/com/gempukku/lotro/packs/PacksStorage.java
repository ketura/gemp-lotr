package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacksStorage {
    private final Map<String, PackBox> _boosterBoxes = new HashMap<>();

    public void addPackBox(String packId, PackBox boosterBox) {
        _boosterBoxes.put(packId, boosterBox);
    }

    public List<CardCollection.Item> openPack(String packId) {
        PackBox boosterBox = GetBox(packId);
        return boosterBox.openPack();
    }

    public Map<String, PackBox> GetAllPacks() {
        return Collections.unmodifiableMap(_boosterBoxes);
    }

    public PackBox GetBox(String packId) {
        PackBox boosterBox = _boosterBoxes.get(packId);
        if (boosterBox == null) {
            try {
                boosterBox = FixedPackBox.LoadFromFile(packId);
                _boosterBoxes.put(packId, boosterBox);
            } catch (IOException exp) {
                return null;
            }
        }
        return boosterBox;
    }
}
