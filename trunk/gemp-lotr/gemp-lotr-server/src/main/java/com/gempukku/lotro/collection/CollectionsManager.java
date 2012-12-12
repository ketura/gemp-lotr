package com.gempukku.lotro.collection;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.packs.PacksStorage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CollectionsManager {
    private static Logger _logger = Logger.getLogger(CollectionsManager.class);
    private ReentrantReadWriteLock _readWriteLock = new ReentrantReadWriteLock();

    private PlayerDAO _playerDAO;
    private CollectionDAO _collectionDAO;
    private TransferDAO _transferDAO;

    private CountDownLatch _collectionReadyLatch = new CountDownLatch(1);
    private DefaultCardCollection _defaultCollection;

    public CollectionsManager(PlayerDAO playerDAO, CollectionDAO collectionDAO, TransferDAO transferDAO, final LotroCardBlueprintLibrary lotroCardBlueprintLibrary) {
        _playerDAO = playerDAO;
        _collectionDAO = collectionDAO;
        _transferDAO = transferDAO;

        _defaultCollection = new DefaultCardCollection();

        // Hunters have 1-194 normal cards, 9 "O" cards, and 3 extra to cover the different culture versions of 15_60

        Thread thr = new Thread() {
            public void run() {
                final int[] cardCounts = new int[]{129, 365, 122, 122, 365, 128, 128, 365, 122, 52, 122, 266, 203, 203, 15, 207, 6, 157, 149, 40};

                for (int i = 0; i <= 19; i++) {
                    _logger.debug("Loading set " + i);
                    for (int j = 1; j <= cardCounts[i]; j++) {
                        String blueprintId = i + "_" + j;
                        try {
                            if (lotroCardBlueprintLibrary.getBaseBlueprintId(blueprintId).equals(blueprintId)) {
                                LotroCardBlueprint cardBlueprint = lotroCardBlueprintLibrary.getLotroCardBlueprint(blueprintId);
                                CardType cardType = cardBlueprint.getCardType();
                                if (cardType == CardType.SITE || cardType == CardType.THE_ONE_RING)
                                    _defaultCollection.addItem(blueprintId, 1);
                                else
                                    _defaultCollection.addItem(blueprintId, 4);
                            }
                        } catch (IllegalArgumentException exp) {

                        }
                    }
                }
                _collectionReadyLatch.countDown();
            }
        };
        thr.start();
    }

    public CardCollection getDefaultCollection() {
        try {
            _collectionReadyLatch.await();
        } catch (InterruptedException exp) {
            throw new RuntimeException("Error while awaiting loading a default colleciton", exp);
        }
        return _defaultCollection;
    }

    public CardCollection getPlayerCollection(Player player, String collectionType) {
        _readWriteLock.readLock().lock();
        try {
            if (collectionType.contains("+"))
                return createSumCollection(player, collectionType.split("\\+"));

            if (collectionType.equals("default"))
                return getDefaultCollection();

            final CardCollection collection = _collectionDAO.getPlayerCollection(player.getId(), collectionType);
            
            if (collection == null && collectionType.equals("permanent"))
                return new DefaultCardCollection();

            return collection;
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get player collection", exp);
        } catch (IOException exp) {
            throw new RuntimeException("Unable to get player collection", exp);
        } finally {
            _readWriteLock.readLock().unlock();
        }
    }

    private CardCollection createSumCollection(Player player, String[] collectionTypes) {
        List<CardCollection> collections = new LinkedList<CardCollection>();
        for (String collectionType : collectionTypes)
            collections.add(getPlayerCollection(player, collectionType));

        return new SumCardCollection(collections);
    }

    private void setPlayerCollection(Player player, String collectionType, CardCollection cardCollection) {
        if (collectionType.contains("+"))
            throw new IllegalArgumentException("Invalid collection type: " + collectionType);
        try {
            _collectionDAO.setPlayerCollection(player.getId(), collectionType, cardCollection);
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to store player collection", exp);
        } catch (IOException exp) {
            throw new RuntimeException("Unable to store player collection", exp);
        }
    }

    public void addPlayerCollection(boolean notifyPlayer, String reason, String player, CollectionType collectionType, CardCollection cardCollection) {
        addPlayerCollection(notifyPlayer, reason, _playerDAO.getPlayer(player), collectionType, cardCollection);
    }

    public void addPlayerCollection(boolean notifyPlayer, String reason, Player player, CollectionType collectionType, CardCollection cardCollection) {
        if (collectionType.getCode().contains("+"))
            throw new IllegalArgumentException("Invalid collection type: " + collectionType);

        _readWriteLock.writeLock().lock();
        try {
            setPlayerCollection(player, collectionType.getCode(), cardCollection);
            _transferDAO.addTransferTo(notifyPlayer, player.getName(), reason, collectionType.getFullName(), cardCollection.getCurrency(), cardCollection);
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public Map<Player, CardCollection> getPlayersCollection(String collectionType) {
        if (collectionType.contains("+"))
            throw new IllegalArgumentException("Invalid collection type: " + collectionType);

        _readWriteLock.readLock().lock();
        try {
            final Map<Integer, CardCollection> playerCollectionsByType = _collectionDAO.getPlayerCollectionsByType(collectionType);

            Map<Player, CardCollection> result = new HashMap<Player, CardCollection>();
            for (Map.Entry<Integer, CardCollection> playerCollection : playerCollectionsByType.entrySet())
                result.put(_playerDAO.getPlayer(playerCollection.getKey()), playerCollection.getValue());

            return result;
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get players collection", exp);
        } catch (IOException exp) {
            throw new RuntimeException("Unable to get players collection", exp);
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
                setPlayerCollection(player, collectionType.getCode(), mutableCardCollection);

                String reason = "Opened pack";
                _transferDAO.addTransferFrom(player.getName(), reason, collectionType.getFullName(), 0, cardCollectionFromBlueprintId(1, packId));
                _transferDAO.addTransferTo(true, player.getName(), reason, collectionType.getFullName(), packContents.getCurrency(), packContents);
            }
            return packContents;
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    private CardCollection cardCollectionFromBlueprintId(int count, String blueprintId) {
        DefaultCardCollection result =new DefaultCardCollection();
        result.addItem(blueprintId, count);
        return result;
    }

    public void addItemsToPlayerCollection(boolean notifyPlayer, String reason, Player player, CollectionType collectionType, Collection<CardCollection.Item> items) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection playerCollection = getPlayerCollection(player, collectionType.getCode());
            if (playerCollection != null) {
                MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
                MutableCardCollection addedCards = new DefaultCardCollection();
                for (CardCollection.Item item : items) {
                    mutableCardCollection.addItem(item.getBlueprintId(), item.getCount());
                    addedCards.addItem(item.getBlueprintId(), item.getCount());
                }

                setPlayerCollection(player, collectionType.getCode(), mutableCardCollection);
                _transferDAO.addTransferTo(notifyPlayer, player.getName(), reason, collectionType.getFullName(), 0, addedCards);
            }
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public void addItemsToPlayerCollection(boolean notifyPlayer, String reason, String player, CollectionType collectionType, Collection<CardCollection.Item> items) {
        addItemsToPlayerCollection(notifyPlayer, reason, _playerDAO.getPlayer(player), collectionType, items);
    }

    public boolean tradeCards(Player player, CollectionType collectionType, String removeBlueprintId, int removeCount, String addBlueprintId, int addCount, int currencyCost) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection playerCollection = getPlayerCollection(player, collectionType.getCode());
            if (playerCollection != null) {
                MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
                if (!mutableCardCollection.removeItem(removeBlueprintId, removeCount))
                    return false;
                if (!mutableCardCollection.removeCurrency(currencyCost))
                    return false;
                mutableCardCollection.addItem(addBlueprintId, addCount);

                setPlayerCollection(player, collectionType.getCode(), mutableCardCollection);

                DefaultCardCollection newCards = new DefaultCardCollection();
                newCards.addItem(addBlueprintId, addCount);

                String reason = "Trading items";
                _transferDAO.addTransferFrom(player.getName(), reason, collectionType.getFullName(), currencyCost, cardCollectionFromBlueprintId(removeCount, removeBlueprintId));
                _transferDAO.addTransferTo(false, player.getName(), reason, collectionType.getFullName(), 0, cardCollectionFromBlueprintId(addCount, addBlueprintId));

                return true;
            }
            return false;
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public boolean buyCardToPlayerCollection(Player player, CollectionType collectionType, String blueprintId, int currency) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection playerCollection = getPlayerCollection(player, collectionType.getCode());
            if (playerCollection != null) {
                MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
                if (!mutableCardCollection.removeCurrency(currency))
                    return false;
                mutableCardCollection.addItem(blueprintId, 1);

                setPlayerCollection(player, collectionType.getCode(), mutableCardCollection);

                String reason = "Items bought";
                _transferDAO.addTransferFrom(player.getName(), reason, collectionType.getFullName(), currency, new DefaultCardCollection());
                _transferDAO.addTransferTo(true, player.getName(), reason, collectionType.getFullName(), 0, cardCollectionFromBlueprintId(1, blueprintId));

                return true;
            }
            return false;
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public boolean sellCardInPlayerCollection(Player player, CollectionType collectionType, String blueprintId, int currency) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection playerCollection = getPlayerCollection(player, collectionType.getCode());
            if (playerCollection != null) {
                MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
                if (!mutableCardCollection.removeItem(blueprintId, 1))
                    return false;
                mutableCardCollection.addCurrency(currency);

                setPlayerCollection(player, collectionType.getCode(), mutableCardCollection);

                _transferDAO.addTransferFrom(player.getName(), "Selling items", collectionType.getFullName(), 0, cardCollectionFromBlueprintId(1, blueprintId));
                _transferDAO.addTransferTo(true, player.getName(), "Selling items", collectionType.getFullName(), currency, new DefaultCardCollection());

                return true;
            }
            return false;
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }

    public void addCurrencyToPlayerCollection(boolean notifyPlayer, String reason, String player, CollectionType collectionType, int currency) {
        addCurrencyToPlayerCollection(notifyPlayer, reason, _playerDAO.getPlayer(player), collectionType, currency);
    }

    public void addCurrencyToPlayerCollection(boolean notifyPlayer, String reason, Player player, CollectionType collectionType, int currency) {
        if (currency > 0) {
            _readWriteLock.writeLock().lock();
            try {
                final CardCollection playerCollection = getPlayerCollection(player, collectionType.getCode());
                if (playerCollection != null) {
                    MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
                    mutableCardCollection.addCurrency(currency);

                    setPlayerCollection(player, collectionType.getCode(), mutableCardCollection);

                    DefaultCardCollection newCurrency = new DefaultCardCollection();
                    newCurrency.addCurrency(currency);

                    _transferDAO.addTransferTo(notifyPlayer, player.getName(), reason, collectionType.getFullName(), currency, new DefaultCardCollection());
                }
            } finally {
                _readWriteLock.writeLock().unlock();
            }
        }
    }

    public boolean removeCurrencyFromPlayerCollection(String reason, Player player, CollectionType collectionType, int currency) {
        _readWriteLock.writeLock().lock();
        try {
            final CardCollection playerCollection = getPlayerCollection(player, collectionType.getCode());
            if (playerCollection != null) {
                MutableCardCollection mutableCardCollection = new DefaultCardCollection(playerCollection);
                if (mutableCardCollection.removeCurrency(currency)) {
                    setPlayerCollection(player, collectionType.getCode(), mutableCardCollection);

                    _transferDAO.addTransferFrom(player.getName(), reason, collectionType.getFullName(), currency, new DefaultCardCollection());

                    return true;
                }
            }
            return false;
        } finally {
            _readWriteLock.writeLock().unlock();
        }
    }
}
