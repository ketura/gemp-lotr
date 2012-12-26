package com.gempukku.lotro.db;

import com.gempukku.lotro.db.vo.GameHistoryEntry;
import com.gempukku.lotro.game.Player;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GameHistoryDAO {
    public void addGameHistory(String winner, String loser, String winReason, String loseReason, String winRecordingId, String loseRecordingId, String formatName, String tournament, String winnerDeckName, String loserDeckName, Date startDate, Date endDate);

    public List<GameHistoryEntry> getGameHistoryForPlayer(Player player, int start, int count);

    public int getGameHistoryForPlayerCount(Player player);

    public int getActivePlayersCount(long from, long duration);

    public int getGamesPlayedCount(long from, long duration);

    public Map<String, Integer> getCasualGamesPlayedPerFormat(long from, long duration);

    public List<PlayerStatistic> getCasualPlayerStatistics(Player player);

    public List<PlayerStatistic> getCompetitivePlayerStatistics(Player player);
}
