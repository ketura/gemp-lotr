package com.gempukku.lotro.logic.timing;

import java.util.Map;

public interface GameResultListener {
    public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons);
}
