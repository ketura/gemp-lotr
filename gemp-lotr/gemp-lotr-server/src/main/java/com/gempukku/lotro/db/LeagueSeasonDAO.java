package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSeason;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeagueSeasonDAO {
    private DbAccess _dbAccess;

    public LeagueSeasonDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public LeagueSeason getSeasonForLeague(League league, int inTime) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("select season_type, max_matches from league_season where league_type=? and start>=? and end<=?");
                try {
                    statement.setString(1, league.getType());
                    statement.setInt(2, inTime);
                    statement.setInt(3, inTime);
                    ResultSet rs = statement.executeQuery();
                    try {
                        if (rs.next()) {
                            String type = rs.getString(1);
                            int maxMatches = rs.getInt(2);
                            return new LeagueSeason(type, maxMatches);
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
