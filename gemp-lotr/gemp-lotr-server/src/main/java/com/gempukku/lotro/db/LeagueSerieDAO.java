package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSerie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LeagueSerieDAO {
    private DbAccess _dbAccess;

    public LeagueSerieDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void addSerie(String leagueType, String seasonType, String format, String collection, int start, int end, int maxMatches) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into league_season (league_type, season_type, format, collection, status, start, end, max_matches) values (?, ?, ?, ?, ?, ?, ?, ?)");
                try {
                    statement.setString(1, leagueType);
                    statement.setString(2, seasonType);
                    statement.setString(3, format);
                    statement.setString(4, collection);
                    statement.setInt(5, 0);
                    statement.setInt(6, start);
                    statement.setInt(7, end);
                    statement.setInt(8, maxMatches);
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

    public void setCollectionGiven(LeagueSerie leagueSerie) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("update league_season set status = 1 where league_type = ? and season_type = ?");
                try {
                    statement.setString(1, leagueSerie.getLeagueType());
                    statement.setString(2, leagueSerie.getType());
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

    public List<LeagueSerie> getSeriesForLeague(League league) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select league_type, season_type, format, collection, status, max_matches, start, end from league_season where league_type=? order by start asc");
                try {
                    statement.setString(1, league.getType());
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<LeagueSerie> seasons = new LinkedList<LeagueSerie>();
                        while (rs.next()) {
                            String leagueType = rs.getString(1);
                            String type = rs.getString(2);
                            String format = rs.getString(3);
                            Map<String, Integer> collection = createCollection(rs.getString(4));
                            int status = rs.getInt(5);
                            int maxMatches = rs.getInt(6);
                            int start = rs.getInt(7);
                            int end = rs.getInt(8);
                            seasons.add(new LeagueSerie(leagueType, type, format, collection, status, maxMatches, start, end));
                        }
                        return seasons;
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

    private Map<String, Integer> createCollection(String collection) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        if (collection != null) {
            final String[] items = collection.split("\n");
            for (String item : items) {
                final String[] xes = item.trim().split("x", 2);
                result.put(xes[1], Integer.parseInt(xes[0]));
            }
        }
        return result;
    }

    public LeagueSerie getSerieForLeague(League league, int inTime) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select league_type, season_type, format, collection, status, max_matches, start, end from league_season where league_type=? and start<=? and end>=?");
                try {
                    statement.setString(1, league.getType());
                    statement.setInt(2, inTime);
                    statement.setInt(3, inTime);
                    ResultSet rs = statement.executeQuery();
                    try {
                        if (rs.next()) {
                            String leagueType = rs.getString(1);
                            String type = rs.getString(2);
                            String format = rs.getString(3);
                            Map<String, Integer> collection = createCollection(rs.getString(4));
                            int status = rs.getInt(5);
                            int maxMatches = rs.getInt(6);
                            int start = rs.getInt(7);
                            int end = rs.getInt(8);
                            return new LeagueSerie(leagueType, type, format, collection, status, maxMatches, start, end);
                        }
                        return null;
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
