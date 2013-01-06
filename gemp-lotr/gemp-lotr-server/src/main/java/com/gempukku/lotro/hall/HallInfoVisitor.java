package com.gempukku.lotro.hall;

import java.util.List;

public interface HallInfoVisitor {
    public enum TableStatus {
        WAITING, PLAYING, FINISHED
    }

    public void serverTime(String time);

    public void playerBusy(boolean busy);

    public void motd(String motd);

    public void visitTable(String tableId, String gameId, boolean watchable, TableStatus status, String statusDescription, String formatName, String tournamentName, List<String> playerIds, String winner);

    public void visitTournamentQueue(String tournamentQueueKey, int cost, String collectionName, String formatName, String tournamentQueueName, String tournamentPrizes,
                                     String pairingDescription, String startCondition, int playerCount, boolean playerSignedUp, boolean joinable);

    public void visitTournament(String tournamentKey, String collectionName, String formatName, String tournamentName, String pairingDescription, String tournamentStage, int round, int playerCount, boolean playerInCompetition);

    public void runningPlayerGame(String gameId);
}
