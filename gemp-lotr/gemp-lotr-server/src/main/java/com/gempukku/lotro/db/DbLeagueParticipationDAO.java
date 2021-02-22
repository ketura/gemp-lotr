package com.gempukku.lotro.db;

import com.gempukku.lotro.game.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DbLeagueParticipationDAO implements LeagueParticipationDAO {
    private DbAccess _dbAccess;

    public DbLeagueParticipationDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void userJoinsLeague(String leagueId, Player player, String remoteAddr) {
        try {
            try (Connection conn = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = conn.prepareStatement("insert into league_participation (league_type, player_name, join_ip) values (?,?,?)")) {
                    statement.setString(1, leagueId);
                    statement.setString(2, player.getName());
                    statement.setString(3, remoteAddr);
                    statement.execute();
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }

    public Collection<String> getUsersParticipating(String leagueId) {
        try {
            try (Connection conn = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = conn.prepareStatement("select player_name from league_participation where league_type=?")) {
                    statement.setString(1, leagueId);
                    try (ResultSet rs = statement.executeQuery()) {
                        Set<String> result = new HashSet<String>();
                        while (rs.next())
                            result.add(rs.getString(1));
                        return result;
                    }
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }
}
