package com.gempukku.lotro.collection;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.Player;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DeliveryService {
    private DeliveryDAO _deliveryDAO;

    private ReadWriteLock _deliveryLock = new ReentrantReadWriteLock(false);

    public DeliveryService(DeliveryDAO deliveryDAO) {
        _deliveryDAO = deliveryDAO;
    }

    public void addPackage(Player player, String reason, String packageName, CardCollection itemCollection) {
        _deliveryLock.writeLock().lock();
        try {
            _deliveryDAO.addPackage(player.getName(), reason, packageName, itemCollection);
        } finally {
            _deliveryLock.writeLock().unlock();
        }
    }

    public boolean hasUndeliveredPackages(String player) {
        _deliveryLock.readLock().lock();
        try {
            return _deliveryDAO.hasUndeliveredPackages(player);
        } finally {
            _deliveryLock.readLock().unlock();
        }
    }

    public Map<String, ? extends CardCollection> consumePackages(Player player) {
        _deliveryLock.writeLock().lock();
        try {
            return _deliveryDAO.consumeUndeliveredPackages(player.getName());
        } finally {
            _deliveryLock.writeLock().unlock();
        }
    }
}
