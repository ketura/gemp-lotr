package com.gempukku.lotro.db;

import com.gempukku.lotro.common.DBDefs;
import com.gempukku.lotro.game.Player;

import java.time.ZonedDateTime;
import java.util.List;

public interface GameHistoryDAO {
    public int addGameHistory(String winner, int winnerId, String loser, int loserId, String winReason, String loseReason, String winRecordingId, String loseRecordingId, String formatName, String tournament, String winnerDeckName, String loserDeckName, ZonedDateTime startDate, ZonedDateTime endDate, int version);
    public DBDefs.GameHistory getGameHistory(String recordID);
    public boolean doesReplayIDExist(String id);
    public List<DBDefs.GameHistory> getGameHistoryForPlayer(Player player, int start, int count);
    public int getGameHistoryForPlayerCount(Player player);

    public List<DBDefs.GameHistory> getGameHistoryForFormat(String format, int count);

    public int getActivePlayersCount(ZonedDateTime from, ZonedDateTime to);

    public int getGamesPlayedCount(ZonedDateTime from, ZonedDateTime to);

    public List<DBDefs.FormatStats> GetAllGameFormatData(ZonedDateTime from, ZonedDateTime to);

    public List<PlayerStatistic> getCasualPlayerStatistics(Player player);

    public List<PlayerStatistic> getCompetitivePlayerStatistics(Player player);

    List<DBDefs.GameHistory> getLastGames(String formatName, int count);
}
