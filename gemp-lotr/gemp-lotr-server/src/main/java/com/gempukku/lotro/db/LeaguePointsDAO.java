package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.league.LeagueSerieData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LeaguePointsDAO {
    private DbAccess _dbAccess;

    public LeaguePointsDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public synchronized void addPoints(League league, LeagueSerieData serie, String playerName, int points) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into league_points (league_type, season_type, player_name, points) values (?, ?, ?, ?)");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, serie.getName());
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

    public Map<String, Points> getLeaguePoints(League league) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select player_name, sum(points), count(*) from league_points where league_type=? group by player_name order by 2 desc, 3 asc");
                try {
                    statement.setString(1, league.getType());
                    ResultSet rs = statement.executeQuery();
                    try {
                        return createPoints(rs);
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

    private Map<String, Points> createPoints(ResultSet rs) throws SQLException {
        Map<String, Points> result = new HashMap<String, Points>();
        while (rs.next()) {
            String playerName = rs.getString(1);
            int sumPoints = rs.getInt(2);
            int gamesPlayed = rs.getInt(3);
            result.put(playerName, new Points(sumPoints, gamesPlayed));
        }
        return result;
    }

    public Map<String, Points> getLeagueSeriePoints(League league, LeagueSerieData serie) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select player_name, sum(points), count(*) from league_points where league_type=? and season_type=? group by player_name order by 2 desc, 3 asc");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, serie.getName());
                    ResultSet rs = statement.executeQuery();
                    try {
                        return createPoints(rs);
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

    public static class Points {
        private int _points;
        private int _gamesPlayed;

        public Points(int points, int gamesPlayed) {
            _points = points;
            _gamesPlayed = gamesPlayed;
        }

        public int getGamesPlayed() {
            return _gamesPlayed;
        }

        public int getPoints() {
            return _points;
        }

        public void addPointsForMatch(int points) {
            _points += points;
            _gamesPlayed++;
        }
    }
}
