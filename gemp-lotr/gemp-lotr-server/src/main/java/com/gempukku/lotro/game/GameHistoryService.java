package com.gempukku.lotro.game;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.db.GameHistoryDAO;
import com.gempukku.lotro.db.vo.GameHistoryEntry;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameHistoryService {
    private GameHistoryDAO _gameHistoryDAO;
    private Map<String, Integer> _playerGameCount = new ConcurrentHashMap<String, Integer>();

    private Map<Long, Integer> _activeCountCachedTimes = new ConcurrentHashMap<Long, Integer>();
    private Map<Long, Integer> _activeCountCachedValues = new ConcurrentHashMap<Long, Integer>();

    private Map<Long, Integer> _gamesPlayedCountCachedTimes = new ConcurrentHashMap<Long, Integer>();
    private Map<Long, Integer> _gamesPlayedCountCachedValues = new ConcurrentHashMap<Long, Integer>();

    public GameHistoryService(GameHistoryDAO gameHistoryDAO) {
        _gameHistoryDAO = gameHistoryDAO;
    }

    public void addGameHistory(String winner, String loser, String winReason, String loseReason, String winRecordingId, String loseRecordingId, String formatName, String winnerDeckName, String loserDeckName, Date startDate, Date endDate) {
        _gameHistoryDAO.addGameHistory(winner, loser, winReason, loseReason, winRecordingId, loseRecordingId, formatName, winnerDeckName, loserDeckName, startDate, endDate);
        Integer winnerCount = _playerGameCount.get(winner);
        Integer loserCount = _playerGameCount.get(loser);
        if (winnerCount != null)
            _playerGameCount.put(winner, winnerCount + 1);
        if (loserCount != null)
            _playerGameCount.put(loser, loserCount + 1);
    }

    public int getGameHistoryForPlayerCount(Player player) {
        Integer result = _playerGameCount.get(player.getName());
        if (result != null)
            return result;
        int count = _gameHistoryDAO.getGameHistoryForPlayerCount(player);
        _playerGameCount.put(player.getName(), count);
        return count;
    }

    public List<GameHistoryEntry> getGameHistoryForPlayer(Player player, int start, int count) {
        return _gameHistoryDAO.getGameHistoryForPlayer(player, start, count);
    }

    public int getActivePlayersInLastMs(long ms) {
        Integer cachedOn = _activeCountCachedTimes.get(ms);
        int minute = DateUtils.getCurrentMinute();
        if (cachedOn != null && minute == cachedOn)
            return _activeCountCachedValues.get(ms);

        int result = _gameHistoryDAO.getActivePlayersInLastMs(ms);
        _activeCountCachedValues.put(ms, result);
        _activeCountCachedTimes.put(ms, minute);
        return result;
    }

    public int getGamesPlayedCountInLastMs(long ms) {
        Integer cachedOn = _gamesPlayedCountCachedTimes.get(ms);
        int minute = DateUtils.getCurrentMinute();
        if (cachedOn != null && minute == cachedOn)
            return _gamesPlayedCountCachedValues.get(ms);

        int result = _gameHistoryDAO.getGamesPlayedCountInLastMs(ms);
        _gamesPlayedCountCachedValues.put(ms, result);
        _gamesPlayedCountCachedTimes.put(ms, minute);
        return result;
    }
}
