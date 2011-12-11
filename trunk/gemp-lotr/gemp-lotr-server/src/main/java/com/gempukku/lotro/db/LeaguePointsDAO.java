package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSeason;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LeaguePointsDAO {
    private DbAccess _dbAccess;

    public LeaguePointsDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void addPoints(League league, LeagueSeason season, String playerName, int points) {
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
}
