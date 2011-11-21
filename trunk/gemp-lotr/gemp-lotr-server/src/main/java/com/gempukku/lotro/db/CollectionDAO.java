package com.gempukku.lotro.db;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.MutableCardCollection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CollectionDAO {
    private DbAccess _dbAccess;
    private CollectionSerializer _collectionSerializer;

    private Map<Integer, Map<String, MutableCardCollection>> _collections = new ConcurrentHashMap<Integer, Map<String, MutableCardCollection>>();

    public CollectionDAO(DbAccess dbAccess, LotroCardBlueprintLibrary library) {
        _dbAccess = dbAccess;
        _collectionSerializer = new CollectionSerializer(library);
    }

    public MutableCardCollection getCollectionForPlayer(Player player, String type) {
        Map<String, MutableCardCollection> playerCollections = _collections.get(player.getId());
        if (playerCollections != null) {
            MutableCardCollection collection = playerCollections.get(type);
            if (collection != null)
                return collection;
        }

        MutableCardCollection collection = getCollectionFromDB(player, type);
        if (collection != null) {
            Map<String, MutableCardCollection> collectionsByType = _collections.get(player.getId());
            if (collectionsByType == null) {
                collectionsByType = new ConcurrentHashMap<String, MutableCardCollection>();
                _collections.put(player.getId(), collectionsByType);
            }
            collectionsByType.put(type, collection);
            return collection;
        }
        return null;
    }

    private MutableCardCollection getCollectionFromDB(Player player, String type) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("select collection from collection where player_id=? and type=?");
                try {
                    statement.setInt(1, player.getId());
                    statement.setString(2, type);
                    ResultSet rs = statement.executeQuery();
                    try {
                        if (rs.next()) {
                            Blob blob = rs.getBlob(1);
                            try {
                                InputStream inputStream = blob.getBinaryStream();
                                try {
                                    return _collectionSerializer.deserializeCollection(inputStream);
                                } finally {
                                    inputStream.close();
                                }
                            } finally {
                                blob.free();
                            }
                        } else {
                            return null;
                        }
                    } finally {
                        rs.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get player collection from DB", exp);
        } catch (IOException exp) {
            throw new RuntimeException("Unable to get player collection from DB", exp);
        }
    }

    public void setCollectionForPlayer(Player player, String type, MutableCardCollection collection) {
        if (!type.equals("default")) {
            storeCollectionToDB(player, type, collection);
            Map<String, MutableCardCollection> collectionsByType = _collections.get(player.getId());
            if (collectionsByType == null) {
                collectionsByType = new ConcurrentHashMap<String, MutableCardCollection>();
                _collections.put(player.getId(), collectionsByType);
            }
            collectionsByType.put(type, collection);
        }
    }

    private void storeCollectionToDB(Player player, String type, CardCollection collection) {
        CardCollection oldCollection = getCollectionFromDB(player, type);

        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                String sql;
                if (oldCollection == null)
                    sql = "insert into collection (collection, player_id, type) values (?, ?, ?)";
                else
                    sql = "update collection set collection=? where player_id=? and type=?";

                PreparedStatement statement = connection.prepareStatement(sql);
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    _collectionSerializer.serializeCollection(collection, baos);

                    statement.setBlob(1, new ByteArrayInputStream(baos.toByteArray()));
                    statement.setInt(2, player.getId());
                    statement.setString(3, type);
                    statement.execute();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get player collection from DB", exp);
        } catch (IOException exp) {
            throw new RuntimeException("Unable to get player collection from DB", exp);
        }
    }
}
