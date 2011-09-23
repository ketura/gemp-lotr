package com.gempukku.lotro.game;

public interface MutableCardCollection extends CardCollection {
    public void addCards(String blueprintId, LotroCardBlueprint blueprint, int count);

    public void addPacks(String packId, int count);
}
