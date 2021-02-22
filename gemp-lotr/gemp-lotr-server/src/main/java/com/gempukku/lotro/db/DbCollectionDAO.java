package com.gempukku.lotro.db;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.game.CardCollection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbCollectionDAO implements CollectionDAO {
    private DbAccess _dbAccess;
    private CollectionSerializer _collectionSerializer;

    public DbCollectionDAO(DbAccess dbAccess, CollectionSerializer collectionSerializer) {
        _dbAccess = dbAccess;
        _collectionSerializer = collectionSerializer;
    }

    public Map<Integer, CardCollection> getPlayerCollectionsByType(String type) throws SQLException, IOException {
        try (Connection connection = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select player_id, collection from collection where type=?")) {
                statement.setString(1, type);
                try (ResultSet rs = statement.executeQuery()) {
                    Map<Integer, CardCollection> playerCollections = new HashMap<Integer, CardCollection>();
                    while (rs.next()) {
                        int playerId = rs.getInt(1);
                        Blob blob = rs.getBlob(2);
                        playerCollections.put(playerId, extractCollectionAndClose(blob));
                    }
                    return playerCollections;
                }
            }
        }
    }

    public CardCollection getPlayerCollection(int playerId, String type) throws SQLException, IOException {
        try (Connection connection = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select collection from collection where player_id=? and type=?")) {
                statement.setInt(1, playerId);
                statement.setString(2, type);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        Blob blob = rs.getBlob(1);
                        return extractCollectionAndClose(blob);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private CardCollection extractCollectionAndClose(Blob blob) throws SQLException, IOException {
        try {
            try (InputStream inputStream = blob.getBinaryStream()) {
                return _collectionSerializer.deserializeCollection(inputStream);
            }
        } finally {
            blob.free();
        }
    }

    public void setPlayerCollection(int playerId, String type, CardCollection collection) throws SQLException, IOException {
        CardCollection oldCollection = getPlayerCollection(playerId, type);

        try (Connection connection = _dbAccess.getDataSource().getConnection()) {
            String sql;
            if (oldCollection == null)
                sql = "insert into collection (collection, player_id, type) values (?, ?, ?)";
            else
                sql = "update collection set collection=? where player_id=? and type=?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                _collectionSerializer.serializeCollection(collection, baos);

                statement.setBlob(1, new ByteArrayInputStream(baos.toByteArray()));
                statement.setInt(2, playerId);
                statement.setString(3, type);
                statement.execute();
            }
        }
    }
}
