package com.gempukku.lotro.hall;

import java.util.Set;

public interface HallInfoVisitor {
    public void playerIsWaiting(boolean waiting);

    public void visitTable(String tableId, String gameId, String tableStatus, Set<String> playerIds);

    public void runningPlayerGame(String gameId);
}
