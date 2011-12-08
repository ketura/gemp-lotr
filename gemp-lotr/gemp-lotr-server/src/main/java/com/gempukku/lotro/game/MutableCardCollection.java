package com.gempukku.lotro.game;

import com.gempukku.lotro.packs.PacksStorage;

import java.util.List;

public interface MutableCardCollection extends CardCollection {
    public void addCards(String blueprintId, LotroCardBlueprint blueprint, int count);

    public void addPacks(String packId, int count);

    public List<Item> openPack(String packId, PacksStorage packBox, LotroCardBlueprintLibrary library);
}
