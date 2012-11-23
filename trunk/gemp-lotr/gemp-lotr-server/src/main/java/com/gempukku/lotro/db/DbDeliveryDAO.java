package com.gempukku.lotro.db;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.collection.DeliveryDAO;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbDeliveryDAO implements DeliveryDAO {
    private DbAccess _dbAccess;
    private CollectionSerializer _collectionSerializer;

    public DbDeliveryDAO(DbAccess dbAccess, CollectionSerializer collectionSerializer) {
        _dbAccess = dbAccess;
        _collectionSerializer = collectionSerializer;
    }

    @Override
    public void addPackage(String player, String reason, String name, CardCollection itemCollection) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                String sql = "insert into delivery (player, reason, name, date, collection, delivered) values (?, ?, ?, ?, ?, ?)";

                PreparedStatement statement = connection.prepareStatement(sql);
                try {
                    statement.setString(1, player);
                    statement.setString(2, reason);
                    statement.setString(3, name);
                    statement.setLong(4, System.currentTimeMillis());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    _collectionSerializer.serializeCollection(itemCollection, baos);
                    statement.setBlob(5, new ByteArrayInputStream(baos.toByteArray()));
                    statement.setInt(6, 0);
                    statement.execute();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to insert new delivery", exp);
        } catch (IOException exp) {
            throw new RuntimeException("Unable to insert new delivery", exp);
        }
    }

    @Override
    public boolean hasUndeliveredPackages(String player) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                String sql = "select count(*) from delivery where player=? and delivered=0";

                PreparedStatement statement = connection.prepareStatement(sql);
                try {
                    statement.setString(1, player);
                    ResultSet resultSet = statement.executeQuery();
                    try {
                        if (resultSet.next())
                            return resultSet.getInt(1) > 0;
                        else
                            return false;
                    } finally {
                        resultSet.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to check if there are any undelivered packages", exp);
        }
    }

    @Override
    public Map<String, ? extends CardCollection> consumeUndeliveredPackages(String player) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                Map<String, DefaultCardCollection> result = new HashMap<String, DefaultCardCollection>();

                String sql = "select name, collection from delivery where player=? and delivered=0";

                PreparedStatement statement = connection.prepareStatement(sql);
                try {
                    statement.setString(1, player);
                    ResultSet resultSet = statement.executeQuery();
                    try {
                        while (resultSet.next()) {
                            String name = resultSet.getString(1);

                            DefaultCardCollection cardCollection = result.get(name);
                            if (cardCollection == null)
                                cardCollection = new DefaultCardCollection();

                            Blob blob = resultSet.getBlob(2);
                            try {
                                InputStream inputStream = blob.getBinaryStream();
                                try {
                                    CardCollection retrieved = _collectionSerializer.deserializeCollection(inputStream);
                                    cardCollection.addCurrency(retrieved.getCurrency());
                                    for (CardCollection.Item item : retrieved.getAll().values())
                                        cardCollection.addItem(item.getBlueprintId(), item.getCount());
                                } finally {
                                    inputStream.close();
                                }
                            } finally {
                                blob.free();
                            }
                            result.put(name, cardCollection);
                        }
                    } finally {
                        resultSet.close();
                    }
                } finally {
                    statement.close();
                }

                sql = "update delivery set delivered=1 where player=?";
                statement = connection.prepareStatement(sql);
                try {
                    statement.setString(1, player);
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
                return result;
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to consume undelivered packages", exp);
        } catch (IOException exp) {
            throw new RuntimeException("Unable to consume undelivered packages", exp);
        }
    }
}
