package com.gempukku.lotro.db;

import com.gempukku.lotro.collection.TransferDAO;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbTransferDAO implements TransferDAO {
    private DbAccess _dbAccess;

    public DbTransferDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    @Override
    public void addTransferFrom(String player, String reason, String collectionName, int currency, CardCollection items) {
        if (currency > 0 || items.getAll().iterator().hasNext()) {
            try {
                try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                    String sql = "insert into transfer (notify, player, reason, name, currency, collection, transfer_date, direction) values (?, ?, ?, ?, ?, ?, ?, 'from')";

                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, 0);
                        statement.setString(2, player);
                        statement.setString(3, reason);
                        statement.setString(4, collectionName);
                        statement.setInt(5, currency);
                        statement.setString(6, serializeCollection(items));
                        statement.setLong(7, System.currentTimeMillis());
                        statement.execute();
                    }
                }
            } catch (SQLException exp) {
                throw new RuntimeException("Unable to add transfer from", exp);
            }
        }
    }

    @Override
    public void addTransferTo(boolean notifyPlayer, String player, String reason, String collectionName, int currency, CardCollection items) {
        if (currency > 0 || items.getAll().iterator().hasNext()) {
            try {
                try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                    String sql = "insert into transfer (notify, player, reason, name, currency, collection, transfer_date, direction) values (?, ?, ?, ?, ?, ?, ?, 'to')";

                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, notifyPlayer ? 1 : 0);
                        statement.setString(2, player);
                        statement.setString(3, reason);
                        statement.setString(4, collectionName);
                        statement.setInt(5, currency);
                        statement.setString(6, serializeCollection(items));
                        statement.setLong(7, System.currentTimeMillis());
                        statement.execute();
                    }
                }
            } catch (SQLException exp) {
                throw new RuntimeException("Unable to add transfer to", exp);
            }
        }
    }

    @Override
    public boolean hasUndeliveredPackages(String player) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                String sql = "select count(*) from transfer where player=? and notify=1";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, player);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next())
                            return resultSet.getInt(1) > 0;
                        else
                            return false;
                    }
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to check if there are any undelivered packages", exp);
        }
    }

    // For now, very naive synchronization
    @Override
    public synchronized Map<String, ? extends CardCollection> consumeUndeliveredPackages(String player) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                Map<String, DefaultCardCollection> result = new HashMap<String, DefaultCardCollection>();

                String sql = "select name, currency, collection from transfer where player=? and notify=1";

                try (PreparedStatement statement = connection.prepareStatement(sql)){
                    statement.setString(1, player);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            String name = resultSet.getString(1);

                            DefaultCardCollection cardCollection = result.get(name);
                            if (cardCollection == null)
                                cardCollection = new DefaultCardCollection();

                            cardCollection.addCurrency(resultSet.getInt(2));
                            CardCollection retrieved = deserializeCollection(resultSet.getString(3));
                            for (CardCollection.Item item : retrieved.getAll())
                                cardCollection.addItem(item.getBlueprintId(), item.getCount());
                            result.put(name, cardCollection);
                        }
                    }
                }

                sql = "update transfer set notify=0 where player=? and notify=1";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, player);
                    statement.executeUpdate();
                }
                return result;
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to consume undelivered packages", exp);
        }
    }

    private String serializeCollection(CardCollection cardCollection) {
        StringBuilder sb = new StringBuilder();
        for (CardCollection.Item item : cardCollection.getAll())
            sb.append(item.getCount()).append("x").append(item.getBlueprintId()).append(",");
        return sb.toString();
    }

    private CardCollection deserializeCollection(String collection) {
        DefaultCardCollection cardCollection = new DefaultCardCollection();
        for (String item : collection.split(",")) {
            if (item.length() > 0) {
                String[] itemSplit = item.split("x", 2);
                int count = Integer.parseInt(itemSplit[0]);
                String blueprintId = itemSplit[1];
                cardCollection.addItem(blueprintId, count);
            }
        }

        return cardCollection;
    }
}
