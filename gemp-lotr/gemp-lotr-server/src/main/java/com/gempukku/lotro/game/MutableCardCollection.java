package com.gempukku.lotro.game;

import com.gempukku.lotro.packs.PacksStorage;

import java.util.Map;

public interface MutableCardCollection extends CardCollection {
    void addItem(String itemId, int count);

    boolean removeItem(String itemId, int count);

    void addCurrency(int currency);

    boolean removeCurrency(int currency);

    CardCollection openPack(String packId, String selection, PacksStorage packBox);

    void setExtraInformation(Map<String, Object> extraInformation);
}
