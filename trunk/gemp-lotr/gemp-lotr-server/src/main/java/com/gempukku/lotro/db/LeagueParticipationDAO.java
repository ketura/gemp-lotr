package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeagueParticipationDAO {
    private DbAccess _dbAccess;

    public LeagueParticipationDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void userJoinsLeague(League league, Player player) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("insert into league_participation (league_type, player_name) values (?,?)");
            try {
                statement.setString(1, league.getType());
                statement.setString(2, player.getName());
                statement.execute();
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }

    public boolean isUserParticipating(League league, Player player) throws SQLException {
        Connection conn = _dbAccess.getDataSource().getConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("select count(*) from league_participation where league_type=? and player_name=?");
            try {
                statement.setString(1, league.getType());
                statement.setString(2, player.getName());
                final ResultSet rs = statement.executeQuery();
                try {
                    if (rs.next())
                        return (rs.getInt(1) > 0);
                    else
                        return false;
                } finally {
                    rs.close();
                }
            } finally {
                statement.close();
            }
        } finally {
            conn.close();
        }
    }
}
