package com.gempukku.lotro.hall;

import java.util.Set;

public interface HallInfoVisitor {
    public void playerIsWaiting(boolean waiting);

    public void visitTable(String tableId, String gameId, boolean noSpectators, String tableStatus, String formatName, String tournamentName, Set<String> playerIds, String winner);

    public void runningPlayerGame(String gameId);

    public void visitTournamentQueue(String tournamentQueueKey, String collectionName, String formatName, String tournamentQueueName, int playerCount, boolean playerSignedUp);
}
