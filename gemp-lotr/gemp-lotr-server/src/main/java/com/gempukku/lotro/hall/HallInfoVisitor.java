package com.gempukku.lotro.hall;

import java.util.Set;

public interface HallInfoVisitor {
    public void visitTable(String tableId, String tableStatus, Set<String> playerIds);

    public void runningPlayerGame(String gameId);
}
