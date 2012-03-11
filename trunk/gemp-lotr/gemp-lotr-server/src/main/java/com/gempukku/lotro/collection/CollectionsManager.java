package com.gempukku.lotro.collection;

import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.MutableCardCollection;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.packs.PacksStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CollectionsManager {
    private ReentrantReadWriteLock _readWriteLock = new ReentrantReadWriteLock();

    private PlayerDAO _playerDAO;
    private CollectionDAO _collectionDAO;
    private DeliveryService _deliveryService;

    public CollectionsManager(PlayerDAO playerDAO, CollectionDAO collectionDAO, DeliveryService deliveryService) {
        _playerDAO = playerDAO;
        _collectionDAO = collectionDAO;
        _deliveryService = deliveryService;
    }

    public void clearDBCache() {
        _collectionDAO.clearCache();
    }

    public CardCollection getPlayerCollection(Player player, String collectionType) {
        _readWriteLock.readLock().lock();
        try {
            final CardCollection collection = _collectionDAO.getCollectionForPlayer(player.getId(), collectionType);
            if (collection == null && collectionType.equals("permanent"))
                return new DefaultCardCollection();
            return collection;
        } finally {
            _readWriteLock.readLock().unlock();
        }
    }

    public void addPlayerCollection(Player player, CollectionType collectionType, CardCollection cardCollection) {
        _readWriteLock.writeLock().lock();
        try {
            _collectionDAO.setCollectionForPlayer(player.getId(), collectionType.getCode(), cardCollection);
            addPackage(player, collectionType, cardCollection);
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public Map<Player, CardCollection> getPlayersCollection(String collectionType) {
        _readWriteLock.readLock().lock();
        try {
            final Map<Integer, CardCollection> playerCollectionsByType = _collectionDAO.getPlayerCollectionsByType(collectionType);

            Map<Player, CardCollection> result = new HashMap<Player, CardCollection>();
            for (Map.Entry<Integer, CardCollection> playerCollection : playerCollectionsByType.entrySet())
                result.put(_playerDAO.getPlayer(playerCollection.getKey()), playerCollection.getValue());

            return result;
        } finally {
            _readWriteLock.readLock().unlock();
        }
    }

    public CardCollection openPackInPlayerCollection(Player player, CollectionType collectionType, String selection, PacksStorage packsStorage, String packId) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection playerCollection = getPlayerCollection(player, collectionType.getCode());
            if (playerCollection == null)
                return null;
            MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
            final CardCollection packContents = mutableCardCollection.openPack(packId, selection, packsStorage);
            if (packContents != null) {
                _collectionDAO.setCollectionForPlayer(player.getId(), collectionType.getCode(), mutableCardCollection);
                addPackage(player, collectionType, packContents);
            }
            return packContents;
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public void addItemsToPlayerCollection(Player player, CollectionType collectionType, Map<String, Integer> items) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection playerCollection = getPlayerCollection(player, collectionType.getCode());
            if (playerCollection != null) {
                MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
                MutableCardCollection addedCards = new DefaultCardCollection();
                for (Map.Entry<String, Integer> item : items.entrySet()) {
                    mutableCardCollection.addItem(item.getKey(), item.getValue());
                    addedCards.addItem(item.getKey(), item.getValue());
                }

                _collectionDAO.setCollectionForPlayer(player.getId(), collectionType.getCode(), mutableCardCollection);
                addPackage(player, collectionType, addedCards);
            }
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public void addItemsToPlayerCollection(String player, CollectionType collectionType, Map<String, Integer> items) {
        addItemsToPlayerCollection(_playerDAO.getPlayer(player), collectionType, items);
    }

    public void addCurrencyToPlayerCollection(Player player, CollectionType collectionType, int currency) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection playerCollection = getPlayerCollection(player, collectionType.getCode());
            if (playerCollection != null) {
                MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
                mutableCardCollection.addCurrency(currency);

                _collectionDAO.setCollectionForPlayer(player.getId(), collectionType.getCode(), mutableCardCollection);
            }
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public void moveCollectionToCollection(String player, CollectionType collectionFrom, CollectionType collectionTo) {
        moveCollectionToCollection(_playerDAO.getPlayer(player), collectionFrom, collectionTo);
    }

    public void moveCollectionToCollection(Player player, CollectionType collectionFrom, CollectionType collectionTo) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection oldCollection = getPlayerCollection(player, collectionFrom.getCode());
            if (oldCollection != null) {
                final CardCollection newCollection = getPlayerCollection(player, collectionTo.getCode());
                if (newCollection != null) {
                    MutableCardCollection mutableCardCollection = new DefaultCardCollection(newCollection);
                    for (Map.Entry<String, Integer> item : oldCollection.getAll().entrySet())
                        mutableCardCollection.addItem(item.getKey(), item.getValue());

                    _collectionDAO.setCollectionForPlayer(player.getId(), collectionTo.getCode(), mutableCardCollection);
                    addPackage(player, collectionTo, oldCollection);
                }
            }
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public boolean commitTrade(CollectionType collectionType, Player playerOne, Player playerTwo, Map<String, Integer> itemsOfPlayerOne, Map<String, Integer> itemsOfPlayerTwo) {
        _readWriteLock.writeLock().lock();
        try {
            CardCollection collectionOne = getPlayerCollection(playerOne, collectionType.getCode());
            CardCollection collectionTwo = getPlayerCollection(playerTwo, collectionType.getCode());

            if (collectionOne == null || collectionTwo == null)
                return false;

            MutableCardCollection playerOneCollection = new DefaultCardCollection(collectionOne);
            MutableCardCollection playerTwoCollection = new DefaultCardCollection(collectionTwo);

            if (!removeItems(playerOneCollection, itemsOfPlayerOne))
                return false;

            if (!removeItems(playerTwoCollection, itemsOfPlayerTwo))
                return false;

            MutableCardCollection addedCardsPlayerOne = new DefaultCardCollection();
            MutableCardCollection addedCardsPlayerTwo = new DefaultCardCollection();

            addItems(playerOneCollection, addedCardsPlayerOne, itemsOfPlayerTwo);
            addItems(playerTwoCollection, addedCardsPlayerTwo, itemsOfPlayerOne);

            _collectionDAO.setCollectionForPlayer(playerOne.getId(), collectionType.getCode(), playerOneCollection);
            _collectionDAO.setCollectionForPlayer(playerTwo.getId(), collectionType.getCode(), playerTwoCollection);

            addPackage(playerOne, collectionType, addedCardsPlayerOne);
            addPackage(playerTwo, collectionType, addedCardsPlayerTwo);

            return true;
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    private boolean removeItems(MutableCardCollection collection, Map<String, Integer> items) {
        for (Map.Entry<String, Integer> item : items.entrySet())
            if (!collection.removeItem(item.getKey(), item.getValue()))
                return false;
        return true;
    }

    private void addItems(MutableCardCollection collection, MutableCardCollection addedCards, Map<String, Integer> items) {
        for (Map.Entry<String, Integer> item : items.entrySet()) {
            collection.addItem(item.getKey(), item.getValue());
            addedCards.addItem(item.getKey(), item.getValue());
        }
    }

    private void addPackage(Player player, CollectionType collectionType, CardCollection cards) {
        _deliveryService.addPackage(player, collectionType.getFullName(), cards);
    }
}
