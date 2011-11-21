package com.gempukku.lotro.cards.packs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultPackBox implements PackBox {
    private Map<String, BoosterBox> _boosterBoxes = new HashMap<String, BoosterBox>();

    public void addBoosterBox(String packId, BoosterBox boosterBox) {
        _boosterBoxes.put(packId, boosterBox);
    }

    @Override
    public List<String> openPack(String packId) {
        BoosterBox boosterBox = _boosterBoxes.get(packId);
        if (boosterBox == null)
            return null;
        return boosterBox.getBooster();
    }
}
