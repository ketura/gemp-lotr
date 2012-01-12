package com.gempukku.lotro.collection;

import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.MutableCardCollection;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.packs.PacksStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CollectionsManager {
    private ReentrantReadWriteLock _readWriteLock = new ReentrantReadWriteLock();

    private PlayerDAO _playerDAO;
    private CollectionDAO _collectionDAO;
    private LeagueService _leagueService;

    public CollectionsManager(PlayerDAO playerDAO, CollectionDAO collectionDAO, LeagueService leagueService) {
        _playerDAO = playerDAO;
        _collectionDAO = collectionDAO;
        _leagueService = leagueService;
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
            if (collection == null) {
                final League league = _leagueService.getLeagueByType(collectionType);
                if (league != null)
                    return league.getBaseCollection();
            }
            return collection;
        } finally {
            _readWriteLock.readLock().unlock();
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

    public CardCollection openPackInPlayerCollection(Player player, String collectionType, String selection, PacksStorage packsStorage, String packId) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection playerCollection = getPlayerCollection(player, collectionType);
            if (playerCollection == null)
                return null;
            MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
            final CardCollection packContents = mutableCardCollection.openPack(packId, selection, packsStorage);
            if (packContents != null)
                _collectionDAO.setCollectionForPlayer(player.getId(), collectionType, mutableCardCollection);
            return packContents;
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public void addItemsToPlayerCollection(Player player, String collectionType, Map<String, Integer> items) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection playerCollection = getPlayerCollection(player, collectionType);
            if (playerCollection != null) {
                MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
                for (Map.Entry<String, Integer> item : items.entrySet())
                    mutableCardCollection.addItem(item.getKey(), item.getValue());

                _collectionDAO.setCollectionForPlayer(player.getId(), collectionType, mutableCardCollection);
            }
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public void moveCollectionToCollection(Player player, String collectionFrom, String collectionTo) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection oldCollection = getPlayerCollection(player, collectionFrom);
            if (oldCollection != null) {
                final CardCollection newCollection = getPlayerCollection(player, collectionTo);
                if (newCollection != null) {
                    MutableCardCollection mutableCardCollection = new DefaultCardCollection(newCollection);
                    for (Map.Entry<String, Integer> item : oldCollection.getAll().entrySet())
                        mutableCardCollection.addItem(item.getKey(), item.getValue());

                    _collectionDAO.setCollectionForPlayer(player.getId(), collectionTo, mutableCardCollection);
                    _collectionDAO.deletePlayerCollection(player.getId(), collectionFrom);
                }
            }
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public boolean commitTrade(String collectionType, Player playerOne, Player playerTwo, Map<String, Integer> itemsOfPlayerOne, Map<String, Integer> itemsOfPlayerTwo) {
        _readWriteLock.writeLock().lock();
        try {
            CardCollection collectionOne = getPlayerCollection(playerOne, collectionType);
            CardCollection collectionTwo = getPlayerCollection(playerTwo, collectionType);

            if (collectionOne == null || collectionTwo == null)
                return false;

            MutableCardCollection playerOneCollection = new DefaultCardCollection(collectionOne);
            MutableCardCollection playerTwoCollection = new DefaultCardCollection(collectionTwo);

            if (!removeItems(playerOneCollection, itemsOfPlayerOne))
                return false;

            if (!removeItems(playerTwoCollection, itemsOfPlayerTwo))
                return false;

            addItems(playerOneCollection, itemsOfPlayerTwo);
            addItems(playerTwoCollection, itemsOfPlayerOne);

            _collectionDAO.setCollectionForPlayer(playerOne.getId(), collectionType, playerOneCollection);
            _collectionDAO.setCollectionForPlayer(playerTwo.getId(), collectionType, playerTwoCollection);

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

    private void addItems(MutableCardCollection collection, Map<String, Integer> items) {
        for (Map.Entry<String, Integer> item : items.entrySet())
            collection.addItem(item.getKey(), item.getValue());
    }
}
