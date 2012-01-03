package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.MutableCardCollection;
import com.gempukku.lotro.game.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DeliveryService {
    private Map<String, Map<String, MutableCardCollection>> _undeliveredDeliverables = new HashMap<String, Map<String, MutableCardCollection>>();

    private ReadWriteLock _deliveryLock = new ReentrantReadWriteLock(false);

    public void addPackage(Player player, String collectionType, CardCollection itemCollection) {
        _deliveryLock.writeLock().lock();
        try {
            Map<String, MutableCardCollection> playerDeliverables = _undeliveredDeliverables.get(player.getName());
            if (playerDeliverables == null) {
                playerDeliverables = new HashMap<String, MutableCardCollection>();
                _undeliveredDeliverables.put(player.getName(), playerDeliverables);
            }
            MutableCardCollection deliverablesInCollection = playerDeliverables.get(collectionType);
            if (deliverablesInCollection == null) {
                deliverablesInCollection = new DefaultCardCollection();
                playerDeliverables.put(collectionType, deliverablesInCollection);
            }
            for (Map.Entry<String, Integer> itemToAdd : itemCollection.getAll().entrySet()) {
                String blueprintId = itemToAdd.getKey();
                int count = itemToAdd.getValue();
                deliverablesInCollection.addItem(blueprintId, count);
            }
        } finally {
            _deliveryLock.writeLock().unlock();
        }
    }

    public boolean hasUndeliveredPackages(String player) {
        _deliveryLock.readLock().lock();
        try {
            return _undeliveredDeliverables.containsKey(player);
        } finally {
            _deliveryLock.readLock().unlock();
        }
    }

    public Map<String, ? extends CardCollection> consumePackages(Player player) {
        _deliveryLock.writeLock().lock();
        try {
            return _undeliveredDeliverables.remove(player.getName());
        } finally {
            _deliveryLock.writeLock().unlock();
        }
    }
}
