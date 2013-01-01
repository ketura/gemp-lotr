package com.gempukku.lotro.hall;

import java.util.List;

public interface HallInfoVisitor {
    public enum TableStatus {
        WAITING, PLAYING, FINISHED
    }

    public void serverTime(String time);

    public void playerBusy(boolean busy);

    public void visitTable(String tableId, String gameId, boolean watchable, TableStatus status, String statusDescription, String formatName, String tournamentName, List<String> playerIds, String winner);

    public void visitTournamentQueue(String tournamentQueueKey, int cost, String collectionName, String formatName, String tournamentQueueName, int playerCount, boolean playerSignedUp);

    public void runningPlayerGame(String gameId);
}
