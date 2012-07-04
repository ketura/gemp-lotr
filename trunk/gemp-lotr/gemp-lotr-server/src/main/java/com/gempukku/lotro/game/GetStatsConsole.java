package com.gempukku.lotro.game;

import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.GameHistoryDAO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GetStatsConsole {
    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy");
    private static final DecimalFormat percFormat = new DecimalFormat("#0.0%");

    public static void main(String[] args) {
        DbAccess dbAccess = new DbAccess();
        GameHistoryDAO gameHistoryDAO = new GameHistoryDAO(dbAccess);
        GameHistoryService gameHistoryService = new GameHistoryService(gameHistoryDAO);
        long startTime = gameHistoryService.getOldestGameHistoryEntry();

        createMonthlyStats(startTime, gameHistoryService);
//        createWeeklyStats(startTime, gameHistoryService);
    }

    private static void createMonthlyStats(long startTime, GameHistoryService gameHistoryService) {
        Date startOfMonth = new Date(startTime);
        startOfMonth.setDate(1);
        startOfMonth.setHours(0);
        startOfMonth.setMinutes(0);
        startOfMonth.setSeconds(0);

        long currentTime = System.currentTimeMillis();

        System.out.println("Monthly statistics:");
        startOfMonth.setTime(startOfMonth.getTime() / 1000L * 1000L);
        long periodStart = startOfMonth.getTime();
        while (periodStart < currentTime) {
            startOfMonth.setMonth(startOfMonth.getMonth() + 1);
            long periodEnd = startOfMonth.getTime();

            System.out.println(monthFormat.format(new Date(periodStart)) + ":");
            GameHistoryStatistics historyStatistics = gameHistoryService.getGameHistoryStatistics(periodStart, periodEnd - periodStart);
            List<GameHistoryStatistics.FormatStat> stats = historyStatistics.getFormatStats();
            for (GameHistoryStatistics.FormatStat stat : stats) {
                System.out.println(stat.getFormat() + ": " + stat.getCount() + " (" + percFormat.format(stat.getPercentage()) + ")");
            }
            System.out.println();

            periodStart = periodEnd;
        }
    }
}
