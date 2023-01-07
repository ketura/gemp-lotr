package com.gempukku.lotro.db;

import com.gempukku.lotro.common.DBDefs;
import com.gempukku.lotro.game.Player;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GameHistoryDAO {
    public void addGameHistory(String winner, String loser, String winReason, String loseReason, String winRecordingId, String loseRecordingId, String formatName, String tournament, String winnerDeckName, String loserDeckName, Date startDate, Date endDate);

    public List<DBDefs.GameHistory> getGameHistoryForPlayer(Player player, int start, int count);

    public int getGameHistoryForPlayerCount(Player player);

    public List<DBDefs.GameHistory> getGameHistoryForFormat(String format, int count);

    public int getActivePlayersCount(long from, long duration);

    public int getGamesPlayedCount(long from, long duration);

    public Map<String, Integer> getCasualGamesPlayedPerFormat(long from, long duration);

    public List<DBDefs.FormatStats> GetAllGameFormatData(long from, long duration);

    public List<PlayerStatistic> getCasualPlayerStatistics(Player player);

    public List<PlayerStatistic> getCompetitivePlayerStatistics(Player player);

    List<DBDefs.GameHistory> getLastGames(String formatName, int count);
}
