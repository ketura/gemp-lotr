package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatch;
import com.gempukku.lotro.league.LeagueSerieData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LeagueMatchDAO {
    private DbAccess _dbAccess;

    public LeagueMatchDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public Collection<LeagueMatch> getLeagueMatches(League league) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select winner, loser from league_match where league_type=?");
                try {
                    statement.setString(1, league.getType());
                    ResultSet rs = statement.executeQuery();
                    try {
                        Set<LeagueMatch> result = new HashSet<LeagueMatch>();
                        while (rs.next()) {
                            String winner = rs.getString(1);
                            String loser = rs.getString(2);

                            result.add(new LeagueMatch(winner, loser));
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

    public Collection<LeagueMatch> getLeagueSerieMatches(League league, LeagueSerieData leagueSerie) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select winner, loser from league_match where league_type=? and season_type=?");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, leagueSerie.getName());
                    ResultSet rs = statement.executeQuery();
                    try {
                        Set<LeagueMatch> result = new HashSet<LeagueMatch>();
                        while (rs.next()) {
                            String winner = rs.getString(1);
                            String loser = rs.getString(2);

                            result.add(new LeagueMatch(winner, loser));
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

    public Collection<LeagueMatch> getPlayerMatchesPlayedOn(League league, LeagueSerieData leagueSeason, String player) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select winner, loser from league_match where league_type=? and season_type=? and (winner=? or loser=?)");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, leagueSeason.getName());
                    statement.setString(3, player);
                    statement.setString(4, player);
                    ResultSet rs = statement.executeQuery();
                    try {
                        Set<LeagueMatch> result = new HashSet<LeagueMatch>();
                        while (rs.next()) {
                            String winner = rs.getString(1);
                            String loser = rs.getString(2);

                            result.add(new LeagueMatch(winner, loser));
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

    public void addPlayedMatch(League league, LeagueSerieData leagueSeason, String winner, String loser) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into league_match (league_type, season_type, winner, loser) values (?, ?, ?, ?)");
                try {
                    statement.setString(1, league.getType());
                    statement.setString(2, leagueSeason.getName());
                    statement.setString(3, winner);
                    statement.setString(4, loser);
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

    private int getCurrentDate() {
        Calendar date = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        return date.get(Calendar.YEAR) * 10000 + (date.get(Calendar.MONTH) + 1) * 100 + date.get(Calendar.DAY_OF_MONTH);
    }
}
