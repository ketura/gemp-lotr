package com.gempukku.lotro.db;

import com.gempukku.lotro.tournament.TournamentMatch;
import com.gempukku.lotro.tournament.TournamentMatchDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbTournamentMatchDAO implements TournamentMatchDAO {
    private DbAccess _dbAccess;

    public DbTournamentMatchDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    @Override
    public void addMatch(String tournamentId, int round, String playerOne, String playerTwo) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("insert into tournament_match (tournament_id, round, player_one, player_two) values (?, ?, ?, ?)");
                try {
                    statement.setString(1, tournamentId);
                    statement.setInt(2, round);
                    statement.setString(3, playerOne);
                    statement.setString(4, playerTwo);
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

    @Override
    public void setMatchResult(String tournamentId, int round, String winner) {
        try {
            Connection conn = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = conn.prepareStatement("update tournament_match set winner=? where tournament_id=? and (player_one=? or player_two=?)");
                try {
                    statement.setString(1, winner);
                    statement.setString(2, tournamentId);
                    statement.setString(3, winner);
                    statement.setString(4, winner);
                    statement.executeUpdate();
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

    @Override
    public List<TournamentMatch> getMatches(String tournamentId, int round) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("select player_one, player_two, winner from tournament_match where tournament_id=? and round=?");
                try {
                    statement.setString(1, tournamentId);
                    statement.setInt(2, round);
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<TournamentMatch> result = new ArrayList<TournamentMatch>();
                        while (rs.next()) {
                            String playerOne = rs.getString(1);
                            String playerTwo = rs.getString(2);
                            String winner = rs.getString(3);

                            result.add(new TournamentMatch(playerOne, playerTwo, winner, round));
                        }
                        return result;
                    } finally {
                        rs.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException(exp);
        }
    }
}
