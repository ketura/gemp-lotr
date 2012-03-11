package com.gempukku.lotro.game;

import com.gempukku.lotro.packs.PacksStorage;

public interface MutableCardCollection extends CardCollection {
    public void addItem(String itemId, int count);

    public boolean removeItem(String itemId, int count);

    public void addCurrency(int currency);

    public boolean removeCurrency(int currency);

    public CardCollection openPack(String packId, String selection, PacksStorage packBox);
}
