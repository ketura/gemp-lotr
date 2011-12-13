package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSerie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LeaguePointsDAO {
    private DbAccess _dbAccess;

    public LeaguePointsDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void addPoints(League league, LeagueSerie season, String playerName, int points) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into league_points (league_type, season_type, player_name, points) values (?, ?, ?, ?)");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, season.getType());
                    statement.setString(3, playerName);
                    statement.setInt(4, points);
                    statement.execute();
                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    public Map<String, Integer> getLeagueStandings(League league) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select player_name, sum(points) from league_points where league_type=? order by 2 desc");
                try {
                    statement.setString(1, league.getType());
                    ResultSet rs = statement.executeQuery();
                    try {
                        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
                        while (rs.next()) {
                            String playerName = rs.getString(1);
                            int sumPoints = rs.getInt(2);
                            result.put(playerName, sumPoints);
                        }
                        return result;
                    } finally {
                        rs.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    public Map<String, Integer> getSerieStandings(League league, LeagueSerie serie) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select player_name, sum(points) from league_points where league_type=? and season_type=? group by player_name order by 2 desc");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, serie.getType());
                    ResultSet rs = statement.executeQuery();
                    try {
                        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
                        while (rs.next()) {
                            String playerName = rs.getString(1);
                            int sumPoints = rs.getInt(2);
                            result.put(playerName, sumPoints);
                        }
                        return result;
                    } finally {
                        rs.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                conn.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }
}
