package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.GameHistoryEntry;
import com.gempukku.lotro.game.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DbGameHistoryDAO implements GameHistoryDAO {
    private DbAccess _dbAccess;

    public DbGameHistoryDAO(DbAccess dbAccess) {
        _dbAccess = dbAccess;
    }

    public void addGameHistory(String winner, String loser, String winReason, String loseReason, String winRecordingId, String loseRecordingId, String formatName, String tournament, String winnerDeckName, String loserDeckName, Date startDate, Date endDate) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("insert into game_history (winner, loser, win_reason, lose_reason, win_recording_id, lose_recording_id, format_name, tournament, winner_deck_name, loser_deck_name, start_date, end_date) values (?,?,?,?,?,?,?,?,?,?,?,?)")) {
                    statement.setString(1, winner);
                    statement.setString(2, loser);
                    statement.setString(3, winReason);
                    statement.setString(4, loseReason);
                    statement.setString(5, winRecordingId);
                    statement.setString(6, loseRecordingId);
                    statement.setString(7, formatName);
                    statement.setString(8, tournament);
                    statement.setString(9, winnerDeckName);
                    statement.setString(10, loserDeckName);
                    statement.setLong(11, startDate.getTime());
                    statement.setLong(12, endDate.getTime());

                    statement.execute();
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get count of player games", exp);
        }
    }

    public List<GameHistoryEntry> getGameHistoryForPlayer(Player player, int start, int count) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("select winner, loser, win_reason, lose_reason, win_recording_id, lose_recording_id, format_name, tournament, winner_deck_name, loser_deck_name, start_date, end_date from game_history where winner=? or loser=? order by end_date desc limit ?, ?")) {
                    statement.setString(1, player.getName());
                    statement.setString(2, player.getName());
                    statement.setInt(3, start);
                    statement.setInt(4, count);
                    try (ResultSet rs = statement.executeQuery()) {
                        List<GameHistoryEntry> result = new LinkedList<GameHistoryEntry>();
                        while (rs.next()) {
                            String winner = rs.getString(1);
                            String loser = rs.getString(2);
                            String winReason = rs.getString(3);
                            String loseReason = rs.getString(4);
                            String winRecordingId = rs.getString(5);
                            String loseRecordingId = rs.getString(6);
                            String formatName = rs.getString(7);
                            String tournament = rs.getString(8);
                            String winnerDeckName = rs.getString(9);
                            String loserDeckName = rs.getString(10);
                            Date startDate = new Date(rs.getLong(11));
                            Date endDate = new Date(rs.getLong(12));

                            GameHistoryEntry entry = new GameHistoryEntry(winner, winReason, winRecordingId, loser, loseReason, loseRecordingId, formatName, tournament, winnerDeckName, loserDeckName, startDate, endDate);
                            result.add(entry);
                        }
                        return result;
                    }
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get count of player games", exp);
        }
    }

    public int getGameHistoryForPlayerCount(Player player) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("select count(*) from game_history where winner=? or loser=?")) {
                    statement.setString(1, player.getName());
                    statement.setString(2, player.getName());
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next())
                            return rs.getInt(1);
                        else
                            return -1;
                    }
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get count of player games", exp);
        }
    }

    public int getActivePlayersCount(long from, long duration) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "select count(*) from (SELECT winner FROM game_history where end_date>=? and end_date<? union select loser from game_history where end_date>=? and end_date<?) as u")) {
                    statement.setLong(1, from);
                    statement.setLong(2, from + duration);
                    statement.setLong(3, from);
                    statement.setLong(4, from + duration);
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next())
                            return rs.getInt(1);
                        else
                            return -1;
                    }
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get count of active players", exp);
        }
    }

    public int getGamesPlayedCount(long from, long duration) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("select count(*) from game_history where end_date>=? and end_date<?")) {
                    statement.setLong(1, from);
                    statement.setLong(2, from + duration);
                    try (ResultSet rs = statement.executeQuery()) {
                        if (rs.next())
                            return rs.getInt(1);
                        else
                            return -1;
                    }
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get count of games played", exp);
        }
    }

    public Map<String, Integer> getCasualGamesPlayedPerFormat(long from, long duration) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("select count(*), format_name from game_history where (tournament is null or tournament like 'Casual %') and end_date>=? and end_date<? group by format_name")) {
                    statement.setLong(1, from);
                    statement.setLong(2, from + duration);
                    Map<String, Integer> result = new HashMap<String, Integer>();
                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next()) {
                            result.put(rs.getString(2), rs.getInt(1));
                        }
                    }
                    return result;
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get count of games played", exp);
        }
    }

    public List<PlayerStatistic> getCasualPlayerStatistics(Player player) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "select deck_name, format_name, sum(win), sum(lose) from" +
                                " (select winner_deck_name as deck_name, format_name, 1 as win, 0 as lose from game_history where winner=? and (tournament is null or tournament like 'Casual %') and (win_reason <> 'Game cancelled due to error')" +
                                " union all select loser_deck_name as deck_name, format_name, 0 as win, 1 as lose from game_history where loser=? and (tournament is null or tournament like 'Casual %') and (win_reason <> 'Game cancelled due to error')) as u" +
                                " group by deck_name, format_name order by format_name, deck_name")) {
                    statement.setString(1, player.getName());
                    statement.setString(2, player.getName());
                    List<PlayerStatistic> result = new LinkedList<PlayerStatistic>();
                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next())
                            result.add(new PlayerStatistic(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
                    }
                    return result;
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get count of games played", exp);
        }
    }

    @Override
    public List<GameHistoryEntry> getLastGames(String requestedFormatName, int count) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "select winner, loser, win_reason, lose_reason, win_recording_id, lose_recording_id, format_name, " +
                                "tournament, winner_deck_name, loser_deck_name, start_date, end_date from game_history " +
                                "where format_name=? order by end_date desc limit ?")) {
                    statement.setString(1, requestedFormatName);
                    statement.setInt(2, count);
                    try (ResultSet rs = statement.executeQuery()) {
                        List<GameHistoryEntry> result = new LinkedList<GameHistoryEntry>();
                        while (rs.next()) {
                            String winner = rs.getString(1);
                            String loser = rs.getString(2);
                            String winReason = rs.getString(3);
                            String loseReason = rs.getString(4);
                            String winRecordingId = rs.getString(5);
                            String loseRecordingId = rs.getString(6);
                            String formatName = rs.getString(7);
                            String tournament = rs.getString(8);
                            String winnerDeckName = rs.getString(9);
                            String loserDeckName = rs.getString(10);
                            Date startDate = new Date(rs.getLong(11));
                            Date endDate = new Date(rs.getLong(12));

                            GameHistoryEntry entry = new GameHistoryEntry(winner, winReason, winRecordingId, loser, loseReason, loseRecordingId, formatName, tournament, winnerDeckName, loserDeckName, startDate, endDate);
                            result.add(entry);
                        }
                        return result;
                    }
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get list of games", exp);
        }
    }

    public List<PlayerStatistic> getCompetitivePlayerStatistics(Player player) {
        try {
            try (Connection connection = _dbAccess.getDataSource().getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "select deck_name, format_name, sum(win), sum(lose) from" +
                                " (select winner_deck_name as deck_name, format_name, 1 as win, 0 as lose from game_history where winner=? and (tournament is not null and not tournament like 'Casual %') and (win_reason <> 'Game cancelled due to error')" +
                                " union all select loser_deck_name as deck_name, format_name, 0 as win, 1 as lose from game_history where loser=? and (tournament is not null and not tournament like 'Casual %') and (win_reason <> 'Game cancelled due to error')) as u" +
                                " group by deck_name, format_name order by format_name, deck_name")) {
                    statement.setString(1, player.getName());
                    statement.setString(2, player.getName());
                    List<PlayerStatistic> result = new LinkedList<PlayerStatistic>();
                    try (ResultSet rs = statement.executeQuery()) {
                        while (rs.next())
                            result.add(new PlayerStatistic(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
                    }
                    return result;
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException("Unable to get count of games played", exp);
        }
    }
}
