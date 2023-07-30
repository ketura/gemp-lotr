package com.gempukku.lotro.game;

import java.util.Map;

public interface GameResultListener {
    public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons);

    public void gameCancelled();
}
