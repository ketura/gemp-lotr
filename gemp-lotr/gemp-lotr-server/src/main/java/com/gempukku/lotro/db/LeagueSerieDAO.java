package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSerie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class LeagueSerieDAO {
    private DbAccess _dbAccess;

    public LeagueSerieDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void addSerie(String leagueType, String seasonType, String format, int start, int end, int maxMatches) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into league_season (league_type, season_type, format, start, end, max_matches) values (?, ?, ?, ?, ?, ?)");
                try {
                    statement.setString(1, leagueType);
                    statement.setString(2, seasonType);
                    statement.setString(3, format);
                    statement.setInt(4, start);
                    statement.setInt(5, end);
                    statement.setInt(6, maxMatches);
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
                PreparedStatement statement = conn.prepareStatement("select season_type, format, max_matches, start, end from league_season where league_type=? order by start asc");
                try {
                    statement.setString(1, league.getType());
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<LeagueSerie> seasons = new LinkedList<LeagueSerie>();
                        while (rs.next()) {
                            String type = rs.getString(1);
                            String format = rs.getString(2);
                            int maxMatches = rs.getInt(3);
                            int start = rs.getInt(4);
                            int end = rs.getInt(5);
                            seasons.add(new LeagueSerie(type, format, maxMatches, start, end));
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

    public LeagueSerie getSerieForLeague(League league, int inTime) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select season_type, format, max_matches, start, end from league_season where league_type=? and start<=? and end>=?");
                try {
                    statement.setString(1, league.getType());
                    statement.setInt(2, inTime);
                    statement.setInt(3, inTime);
                    ResultSet rs = statement.executeQuery();
                    try {
                        if (rs.next()) {
                            String type = rs.getString(1);
                            String format = rs.getString(2);
                            int maxMatches = rs.getInt(3);
                            int start = rs.getInt(4);
                            int end = rs.getInt(5);
                            return new LeagueSerie(type, format, maxMatches, start, end);
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
