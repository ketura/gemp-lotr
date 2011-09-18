package com.gempukku.lotro.logic.timing;

import java.util.Set;

public interface GameResultListener {
    public void gameFinished(String winnerPlayerId, Set<String> loserPlayerIds, String reason);
}
