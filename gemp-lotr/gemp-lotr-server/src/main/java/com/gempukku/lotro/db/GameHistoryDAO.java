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

    public void addGameHistory(String winner, String loser, String winReason, String loseReason, String winRecordingId, String loseRecordingId, Date startDate, Date endDate) {
        try {
            Connection connection = _dbAccess.getDataSource().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("insert into game_history (winner, loser, win_reason, lose_reason, win_recording_id, lose_recording_id, start_date, end_date) values (?,?,?,?,?,?,?,?)");
                try {
                    statement.setString(1, winner);
                    statement.setString(2, loser);
                    statement.setString(3, winReason);
                    statement.setString(4, loseReason);
                    statement.setString(5, winRecordingId);
                    statement.setString(6, loseRecordingId);
                    statement.setLong(7, startDate.getTime());
                    statement.setLong(8, endDate.getTime());

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
                PreparedStatement statement = connection.prepareStatement("select winner, loser, win_reason, lose_reason, win_recording_id, lose_recording_id, start_date, end_date from game_history where winner=? or loser=? order by end_date deck limit ?, ?");
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
                            Date startDate = new Date(rs.getLong(7));
                            Date endDate = new Date(rs.getLong(8));

                            GameHistoryEntry entry = new GameHistoryEntry(winner, winReason, winRecordingId, loser, loseReason, loseRecordingId, startDate, endDate);
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
}
