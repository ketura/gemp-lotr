package com.gempukku.lotro.logic.timing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public interface GameResultListener {
    public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) throws SQLException, IOException;

    public void gameCancelled();
}
