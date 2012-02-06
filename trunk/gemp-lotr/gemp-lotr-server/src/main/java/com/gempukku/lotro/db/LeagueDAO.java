package com.gempukku.lotro.db;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.db.vo.League;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class LeagueDAO {
    private DbAccess _dbAccess;

    private CollectionSerializer _serializer;

    private int _dateLoaded;

    public LeagueDAO(DbAccess dbAccess, CollectionSerializer serializer) {
        _dbAccess = dbAccess;
        _serializer = serializer;
    }

    public void clearCache() {
        _dateLoaded = 0;
    }

    public void addLeague(String name, String type, int startTime, int endTime) throws SQLException, IOException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("insert into league (name, type, start, end) values (?, ?, ?, ?)");
            try {
                statement.setString(1, name);
                statement.setString(2, type);
                statement.setInt(3, startTime);
                statement.setInt(4, endTime);
                statement.execute();
                _dateLoaded = 0;
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    public Set<League> loadActiveLeagues(int currentTime) throws SQLException, IOException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select id, name, type, start, end from league where start<=? and end>=? order by start desc");
            try {
                statement.setInt(1, currentTime);
                statement.setInt(2, currentTime);
                ResultSet rs = statement.executeQuery();
                try {
                    Set<League> activeLeagues = new HashSet<League>();
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String type = rs.getString(3);
                        int start = rs.getInt(4);
                        int end = rs.getInt(5);
                        activeLeagues.add(new League(id, type, name, start, end));
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
}
