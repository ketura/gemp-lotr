package com.gempukku.lotro.game.state;

import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.game.ActionsEnvironment;
import com.gempukku.lotro.logic.modifiers.ModifiersEnvironment;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public interface LotroGame {
    public GameState getGameState();

    public ModifiersEnvironment getModifiersEnvironment();

    public ModifiersQuerying getModifiersQuerying();

    public ActionsEnvironment getActionsEnvironment();

    public UserFeedback getUserFeedback();

    public void checkWinLoseConditions();

    public void playerWon(String currentPlayerId, String reason);

    public void playerLost(String currentPlayerId, String reason);
}
