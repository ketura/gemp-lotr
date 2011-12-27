package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSerie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LeaguePointsDAO {
    private DbAccess _dbAccess;

    private Map<League, List<Standing>> _leagueStandings = new ConcurrentHashMap<League, List<Standing>>();
    private Map<LeagueSerie, List<Standing>> _serieStandings = new ConcurrentHashMap<LeagueSerie, List<Standing>>();

    public LeaguePointsDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public synchronized void addPoints(League league, LeagueSerie serie, String playerName, int points) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into league_points (league_type, season_type, player_name, points) values (?, ?, ?, ?)");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, serie.getType());
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

        _leagueStandings.remove(league);
        _serieStandings.remove(serie);
    }

    public List<Standing> getLeagueStandings(League league) {
        List<Standing> standings = _leagueStandings.get(league);
        if (standings == null) {
            synchronized (this) {
                standings = loadLeagueStandings(league);
                _leagueStandings.put(league, standings);
            }
        }
        return standings;
    }

    private List<Standing> loadLeagueStandings(League league) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select player_name, sum(points), count(*) from league_points where league_type=? group by player_name order by 2 desc");
                try {
                    statement.setString(1, league.getType());
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<Standing> result = new LinkedList<Standing>();
                        while (rs.next()) {
                            String playerName = rs.getString(1);
                            int sumPoints = rs.getInt(2);
                            int gamesPlayed = rs.getInt(3);
                            result.add(new Standing(playerName, sumPoints, gamesPlayed));
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

    public List<Standing> getSerieStandings(League league, LeagueSerie serie) {
        List<Standing> standings = _serieStandings.get(serie);
        if (standings == null) {
            synchronized (this) {
                standings = loadSerieStandings(league, serie);
                _serieStandings.put(serie, standings);
            }
        }
        return standings;
    }

    private List<Standing> loadSerieStandings(League league, LeagueSerie serie) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select player_name, sum(points), count(*) from league_points where league_type=? and season_type=? group by player_name order by 2 desc, 3 asc");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, serie.getType());
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<Standing> result = new LinkedList<Standing>();
                        while (rs.next()) {
                            String playerName = rs.getString(1);
                            int sumPoints = rs.getInt(2);
                            int gamesPlayed = rs.getInt(3);
                            result.add(new Standing(playerName, sumPoints, gamesPlayed));
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

    public static class Standing {
        private String _player;
        private int _points;
        private int _gamesPlayed;

        public Standing(String player, int points, int gamesPlayed) {
            _player = player;
            _points = points;
            _gamesPlayed = gamesPlayed;
        }

        public int getGamesPlayed() {
            return _gamesPlayed;
        }

        public String getPlayer() {
            return _player;
        }

        public int getPoints() {
            return _points;
        }
    }
}
