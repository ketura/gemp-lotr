package com.gempukku.lotro.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

public class DbIgnoreDAO implements IgnoreDAO {
    private DbAccess dbAccess;

    public DbIgnoreDAO(DbAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

    @Override
    public Set<String> getIgnoredUsers(String playerId) {
        try {
            try (Connection connection = dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("select ignoredName from ignores where playerName=?")) {
                    statement.setString(1, playerId);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        Set<String> ignoredUsers = new TreeSet<>();
                        while (resultSet.next()) {
                            ignoredUsers.add(resultSet.getString(1));
                        }
                        return ignoredUsers;
                    }
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get ignored users", exp);
        }
    }

    @Override
    public boolean addIgnoredUser(String playerId, String ignoredName) {
        try {
            try (Connection connection = dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("insert into ignores (playerName, ignoredName) values (?, ?)")) {
                    statement.setString(1, playerId);
                    statement.setString(2, ignoredName);

                    statement.execute();
                    return true;
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to add ignored user", exp);
        }
    }

    @Override
    public boolean removeIgnoredUser(String playerId, String ignoredName) {
        try {
            try (Connection connection = dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("delete from ignores where playerName=? and ignoredName=?")) {
                    statement.setString(1, playerId);
                    statement.setString(2, ignoredName);

                    statement.execute();
                    return true;
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to remove ignored user", exp);
        }
    }
}
