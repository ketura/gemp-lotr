package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatch;

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

    public Collection<LeagueMatch> getPlayerMatchesPlayedOn(League league, String player, int datePlayed) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select winner, loser from leaguematch where league_id=? and date=? and (winner=? or loser=?)");
                try {
                    statement.setInt(1, league.getId());
                    statement.setInt(2, datePlayed);
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

    public void addPlayedMatch(League league, String winner, String loser, int datePlayed) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into leaguematch (league_id, date, winner, loser) values (?, ?, ?, ?)");
                try {
                    statement.setInt(1, league.getId());
                    statement.setInt(2, datePlayed);
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
