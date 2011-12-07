package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.GameHistoryEntry;
import com.gempukku.lotro.db.vo.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GameHistoryDAO {
    private DbAccess _dbAccess;

    public GameHistoryDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void addGameHistory(String winner, String loser, String winReason, String loseReason, String winRecordingId, String loseRecordingId, String formatName, String winnerDeckName, String loserDeckName, Date startDate, Date endDate) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("insert into game_history (winner, loser, win_reason, lose_reason, win_recording_id, lose_recording_id, format_name, winner_deck_name, loser_deck_name, start_date, end_date) values (?,?,?,?,?,?,?,?,?,?,?)");
                try {
                    statement.setString(1, winner);
                    statement.setString(2, loser);
                    statement.setString(3, winReason);
                    statement.setString(4, loseReason);
                    statement.setString(5, winRecordingId);
                    statement.setString(6, loseRecordingId);
                    statement.setString(7, formatName);
                    statement.setString(8, winnerDeckName);
                    statement.setString(9, loserDeckName);
                    statement.setLong(10, startDate.getTime());
                    statement.setLong(11, endDate.getTime());

                    statement.execute();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get count of player games", exp);
        }
    }

    public List<GameHistoryEntry> getGameHistoryForPlayer(Player player, int start, int count) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("select winner, loser, win_reason, lose_reason, win_recording_id, lose_recording_id, format_name, winner_deck_name, loser_deck_name, start_date, end_date from game_history where winner=? or loser=? order by end_date desc limit ?, ?");
                try {
                    statement.setString(1, player.getName());
                    statement.setString(2, player.getName());
                    statement.setInt(3, start);
                    statement.setInt(4, count);
                    ResultSet rs = statement.executeQuery();
                    try {
                        List<GameHistoryEntry> result = new LinkedList<GameHistoryEntry>();
                        while (rs.next()) {
                            String winner = rs.getString(1);
                            String loser = rs.getString(2);
                            String winReason = rs.getString(3);
                            String loseReason = rs.getString(4);
                            String winRecordingId = rs.getString(5);
                            String loseRecordingId = rs.getString(6);
                            String formatName = rs.getString(7);
                            String winnerDeckName = rs.getString(8);
                            String loserDeckName = rs.getString(9);
                            Date startDate = new Date(rs.getLong(10));
                            Date endDate = new Date(rs.getLong(11));

                            GameHistoryEntry entry = new GameHistoryEntry(winner, winReason, winRecordingId, loser, loseReason, loseRecordingId, formatName, winnerDeckName, loserDeckName, startDate, endDate);
                            result.add(entry);
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
            throw new RuntimeException("Unable to get count of player games", exp);
        }
    }

    public int getGameHistoryForPlayerCount(Player player) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("select count(*) from game_history where winner=? or loser=?");
                try {
                    statement.setString(1, player.getName());
                    statement.setString(2, player.getName());
                    ResultSet rs = statement.executeQuery();
                    try {
                        if (rs.next())
                            return rs.getInt(1);
                        else
                            return -1;
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
            throw new RuntimeException("Unable to get count of player games", exp);
        }
    }

    public int getGamesPlayedCountInLastMs(long ms) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                // 5 minutes minimum game
                long minTime = 1000 * 60 * 5;
                PreparedStatement statement = connection.prepareStatement("select count(*) from game_history where end_date>=? and end_date-start_date>" + minTime);
                try {
                    statement.setLong(1, System.currentTimeMillis() - ms);
                    ResultSet rs = statement.executeQuery();
                    try {
                        if (rs.next())
                            return rs.getInt(1);
                        else
                            return -1;
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
            throw new RuntimeException("Unable to get count of games played", exp);
        }
    }
}
