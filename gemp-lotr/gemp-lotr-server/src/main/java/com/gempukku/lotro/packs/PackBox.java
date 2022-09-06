package com.gempukku.lotro.packs;

import com.gempukku.lotro.game.CardCollection;

import java.util.List;

public interface PackBox {
    public List<CardCollection.Item> openPack();
    public List<CardCollection.Item> openPack(int selection);
    public List<String> GetAllOptions();
}
