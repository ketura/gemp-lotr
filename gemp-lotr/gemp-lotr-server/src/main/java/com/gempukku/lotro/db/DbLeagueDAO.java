package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbLeagueDAO implements LeagueDAO {
    private DbAccess _dbAccess;

    public DbLeagueDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void addLeague(int cost, String name, String type, String clazz, String parameters, int start, int endTime) throws SQLException, IOException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("insert into league (name, type, class, parameters, start, end, status, cost) values (?, ?, ?, ?, ?, ?, ?, ?)")) {
                statement.setString(1, name);
                statement.setString(2, type);
                statement.setString(3, clazz);
                statement.setString(4, parameters);
                statement.setInt(5, start);
                statement.setInt(6, endTime);
                statement.setInt(7, 0);
                statement.setInt(8, cost);
                statement.execute();
            }
        }
    }

    public List<League> loadActiveLeagues(int currentTime) throws SQLException, IOException {
        try (Connection conn = _dbAccess.getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement("select name, type, class, parameters, status, cost from league where end>=? order by start desc")) {
                statement.setInt(1, currentTime);
                try (ResultSet rs = statement.executeQuery()) {
                    List<League> activeLeagues = new ArrayList<League>();
                    while (rs.next()) {
                        String name = rs.getString(1);
                        String type = rs.getString(2);
                        String clazz = rs.getString(3);
                        String parameters = rs.getString(4);
                        int status = rs.getInt(5);
                        int cost = rs.getInt(6);
                        activeLeagues.add(new League(cost, name, type, clazz, parameters, status));
                    }
                    return activeLeagues;
                }
            }
        }
    }

    public void setStatus(League league, int newStatus) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                String sql = "update league set status=? where type=?";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, newStatus);
                    statement.setString(2, league.getType());
                    statement.execute();
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to update league status", exp);
        }
    }
}
