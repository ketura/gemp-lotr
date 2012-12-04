package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;

import java.util.Map;

public interface TransferDAO {
    public boolean hasUndeliveredPackages(String player);
    public Map<String, ? extends CardCollection> consumeUndeliveredPackages(String player);

    public void addTransferTo(boolean notifyPlayer, String player, String reason, String collectionName, int currency, CardCollection items);
    public void addTransferFrom(String player, String reason, String collectionName, int currency, CardCollection items);
}
