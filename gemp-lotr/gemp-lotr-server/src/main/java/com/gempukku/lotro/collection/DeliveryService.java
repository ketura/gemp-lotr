package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DeliveryService {
    private Map<String, Map<String, List<CardCollection.Item>>> _undeliveredDeliverables = new HashMap<String, Map<String, List<CardCollection.Item>>>();

    private ReadWriteLock _deliveryLock = new ReentrantReadWriteLock(false);

    public void addPackage(Player player, String collectionType, List<CardCollection.Item> items) {
        _deliveryLock.writeLock().lock();
        try {
            Map<String, List<CardCollection.Item>> playerDeliverables = _undeliveredDeliverables.get(player.getName());
            if (playerDeliverables == null) {
                playerDeliverables = new HashMap<String, List<CardCollection.Item>>();
                _undeliveredDeliverables.put(player.getName(), playerDeliverables);
            }
            List<CardCollection.Item> deliverablesInCollection = playerDeliverables.get(collectionType);
            if (deliverablesInCollection == null) {
                deliverablesInCollection = new LinkedList<CardCollection.Item>();
                playerDeliverables.put(collectionType, deliverablesInCollection);
            }
            deliverablesInCollection.addAll(items);
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

    public Map<String, List<CardCollection.Item>> consumePackages(Player player) {
        _deliveryLock.writeLock().lock();
        try {
            return _undeliveredDeliverables.remove(player.getName());
        } finally {
            _deliveryLock.writeLock().unlock();
        }
    }
}
