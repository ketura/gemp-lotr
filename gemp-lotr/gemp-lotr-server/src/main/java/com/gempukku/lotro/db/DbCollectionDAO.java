package com.gempukku.lotro.db;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.game.CardCollection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbCollectionDAO implements CollectionDAO {
    private final DbAccess _dbAccess;
    private final CollectionSerializer _collectionSerializer;

    public DbCollectionDAO(DbAccess dbAccess, CollectionSerializer collectionSerializer) {
        _dbAccess = dbAccess;
        _collectionSerializer = collectionSerializer;
    }

    public Map<Integer, CardCollection> getPlayerCollectionsByType(String type) throws SQLException, IOException {
        try (Connection connection = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select player_id, collection from collection where type=?")) {
                statement.setString(1, type);
                try (ResultSet rs = statement.executeQuery()) {
                    Map<Integer, CardCollection> playerCollections = new HashMap<>();
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

    private class intContainer {
        public int ID;
    }

    public int getCollectionID(int playerId, String type)  {
        try {

            Sql2o db = new Sql2o(_dbAccess.getDataSource());

            try (org.sql2o.Connection conn = db.open()) {
                String sql = """
                        SELECT
                            ID
                        FROM collection
                        WHERE type = :type
                            AND player_id = :playerID
                        LIMIT 1
                        """;
                intContainer result = conn.createQuery(sql)
                        .addParameter("type", type)
                        .addParameter("playerID", playerId)
                        .executeAndFetch(intContainer.class)
                        .stream().findFirst().orElse(null);

                return result.ID;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Unable to retrieve collection ID", ex);
        }
    }

    public void updateCollection(int playerId, String type, CardCollection collection, String source) {
        String sql = """
                        INSERT INTO collection_entries(collection_id, quantity, product_type, product, source)
                        VALUES (:collid, :quantity, :type, :product, :source)
                        ON DUPLICATE KEY UPDATE quantity = :quantity, source = :source;
                        """;
        String error = "Unable to update product via upsert into collection_entries.";
        updateCollection(playerId, type, collection, source, sql, error);
    }

    public void addToCollection(int playerId, String type, CardCollection collection, String source) {
        String sql = """
                        INSERT INTO collection_entries(collection_id, quantity, product_type, product, source)
                        VALUES (:collid, :quantity, :type, :product, :source)
                        ON DUPLICATE KEY UPDATE quantity = quantity + :quantity, source = :source;
                        """;
        String error = "Unable to add product via upsert into collection_entries.";
        updateCollection(playerId, type, collection, source, sql, error);
    }

    public void removeFromCollection(int playerId, String type, CardCollection collection, String source) {
        String sql = """
                        INSERT INTO collection_entries(collection_id, quantity, product_type, product, source)
                        VALUES (:collid, :quantity, :type, :product, :source)
                        ON DUPLICATE KEY UPDATE quantity = GREATEST(quantity - :quantity, 0), source = :source;
                        """;
        String error = "Unable to remove product via upsert into collection_entries.";
        updateCollection(playerId, type, collection, source, sql, error);
    }

    public void convertCollection(int playerId, String type) throws SQLException, IOException {
        CardCollection oldCollection = getPlayerCollection(playerId, type);
        updateCollection(playerId, type, oldCollection, "Initial Convert");
    }


    //TODO:
    // - Convert currency to an extra info entry
    // - add data field to the original collection table to hold the extra info as json
    // - create player-looping function to convert all collections and see if it blows up via test
    // - add CollectionsManager mirror functions to read/writing into the new table
    // - write unit tests to convert and compare a bajillion collections
    // - sunset the old collection handling functions
    // - test a: draft, new art card reward, my cards reward, pack openings
    // - write script to back up db and delete the old binary blob column
    // - write script to convert all leagues to use IDs instead of names
    private void updateCollection(int playerId, String type, CardCollection collection, String source, String sql, String error) {
        int collID = getCollectionID(playerId, type);

        try {
            Sql2o db = new Sql2o(_dbAccess.getDataSource());

            try (org.sql2o.Connection conn = db.beginTransaction()) {
                Query query = conn.createQuery(sql, true);
                int i = 0;
                //TODO: maybe detect when the batch is 1000 entries long and execute then to prevent errors
                for(var card : collection.getAll()) {
                    query.addParameter("collid", collID)
                            .addParameter("quantity", card.getCount())
                            .addParameter("type", card.getType())
                            .addParameter("product", card.getBlueprintId())
                            .addParameter("source", source)
                            .addToBatch();
                }
                query.executeBatch();
                conn.commit();
            }
        } catch (Exception ex) {
            throw new RuntimeException(error, ex);
        }
    }


}
