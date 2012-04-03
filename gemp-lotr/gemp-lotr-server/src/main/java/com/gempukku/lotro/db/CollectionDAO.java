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

public class CollectionDAO {
    private DbAccess _dbAccess;
    private CollectionSerializer _collectionSerializer;

    public CollectionDAO(DbAccess dbAccess, CollectionSerializer collectionSerializer) {
        _dbAccess = dbAccess;
        _collectionSerializer = collectionSerializer;
    }

    public Map<Integer, CardCollection> getPlayerCollectionsByType(String type) throws SQLException, IOException {
        Connection connection = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("select player_id, collection from collection where type=?");
            try {
                statement.setString(1, type);
                ResultSet rs = statement.executeQuery();
                try {
                    Map<Integer, CardCollection> playerCollections = new HashMap<Integer, CardCollection>();
                    while (rs.next()) {
                        int playerId = rs.getInt(1);
                        Blob blob = rs.getBlob(2);
                        try {
                            InputStream inputStream = blob.getBinaryStream();
                            try {
                                playerCollections.put(playerId, _collectionSerializer.deserializeCollection(inputStream));
                            } finally {
                                inputStream.close();
                            }
                        } finally {
                            blob.free();
                        }
                    }
                    return playerCollections;
                } finally {
                    rs.close();
                }
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
    }

    public CardCollection getPlayerCollection(int playerId, String type) throws SQLException, IOException {
        Connection connection = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("select collection from collection where player_id=? and type=?");
            try {
                statement.setInt(1, playerId);
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
    }

    public void setPlayerCollection(int playerId, String type, CardCollection collection) throws SQLException, IOException {
        CardCollection oldCollection = getPlayerCollection(playerId, type);

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
                statement.setInt(2, playerId);
                statement.setString(3, type);
                statement.execute();
            } finally {
                statement.close();
            }
        } finally {
            connection.close();
        }
    }
}
