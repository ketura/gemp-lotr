package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;

import java.util.Map;

public interface DeliveryDAO {
    public boolean hasUndeliveredPackages(String player);
    public void addPackage(String player, String reason, String name, CardCollection itemCollection);
    public Map<String, ? extends CardCollection> consumeUndeliveredPackages(String player);
}
