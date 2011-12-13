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

    public void addSerie(String leagueType, String seasonType, int start, int end, int maxMatches) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into league_season (league_type, season_type, start, end, max_matches) values (?, ?, ?, ?, ?)");
                try {
                    statement.setString(1, leagueType);
                    statement.setString(2, seasonType);
                    statement.setInt(3, start);
                    statement.setInt(4, end);
                    statement.setInt(5, maxMatches);
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
                PreparedStatement statement = conn.prepareStatement("select season_type, max_matches, start, end from league_season where league_type=? order by start asc");
                try {
                    statement.setString(1, league.getType());
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<LeagueSerie> seasons = new LinkedList<LeagueSerie>();
                        while (rs.next()) {
                            String type = rs.getString(1);
                            int maxMatches = rs.getInt(2);
                            int start = rs.getInt(3);
                            int end = rs.getInt(4);
                            seasons.add(new LeagueSerie(type, maxMatches, start, end));
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
                PreparedStatement statement = conn.prepareStatement("select season_type, max_matches, start, end from league_season where league_type=? and start<=? and end>=?");
                try {
                    statement.setString(1, league.getType());
                    statement.setInt(2, inTime);
                    statement.setInt(3, inTime);
                    ResultSet rs = statement.executeQuery();
                    try {
                        if (rs.next()) {
                            String type = rs.getString(1);
                            int maxMatches = rs.getInt(2);
                            int start = rs.getInt(3);
                            int end = rs.getInt(4);
                            return new LeagueSerie(type, maxMatches, start, end);
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
