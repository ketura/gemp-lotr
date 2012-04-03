package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeagueDAO {
    private DbAccess _dbAccess;

    public LeagueDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void addLeague(String name, String type, String clazz, String parameters, int startTime, int endTime) throws SQLException, IOException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("insert into league (name, type, class, parameters, start, end, status) values (?, ?, ?, ?, ?, ?, ?)");
            try {
                statement.setString(1, name);
                statement.setString(2, type);
                statement.setString(3, clazz);
                statement.setString(4, parameters);
                statement.setInt(5, startTime);
                statement.setInt(6, endTime);
                statement.setInt(7, 0);
                statement.execute();
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    public List<League> loadActiveLeagues(int currentTime) throws SQLException, IOException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, name, type, class, parameters, start, end, status from league where start<=? and end>=? order by start desc");
            try {
                statement.setInt(1, currentTime);
                statement.setInt(2, currentTime);
                ResultSet rs = statement.executeQuery();
                try {
                    List<League> activeLeagues = new ArrayList<League>();
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String type = rs.getString(3);
                        String clazz = rs.getString(4);
                        String parameters = rs.getString(5);
                        int start = rs.getInt(6);
                        int end = rs.getInt(7);
                        int status = rs.getInt(8);
                        activeLeagues.add(new League(id, name, type, clazz, parameters, start, end, status));
                    }
                    return activeLeagues;
                } finally {
                    rs.close();
                }
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    public void setStatus(League league, int newStatus) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                String sql = "update league set status=? where id=?";

                PreparedStatement statement = connection.prepareStatement(sql);
                try {
                    statement.setInt(1, newStatus);
                    statement.setInt(2, league.getId());
                    statement.execute();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to update league status", exp);
        }
    }
}
