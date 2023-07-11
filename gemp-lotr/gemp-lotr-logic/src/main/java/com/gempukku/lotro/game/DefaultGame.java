package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.communication.UserFeedback;
import com.gempukku.lotro.cards.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.actions.ActionsEnvironment;
import com.gempukku.lotro.game.modifiers.ModifiersEnvironment;
import com.gempukku.lotro.game.modifiers.ModifiersQuerying;

public interface DefaultGame {
    public GameState getGameState();

    public LotroCardBlueprintLibrary getLotroCardBlueprintLibrary();

    public ModifiersEnvironment getModifiersEnvironment();

    public ModifiersQuerying getModifiersQuerying();

    public ActionsEnvironment getActionsEnvironment();

    public UserFeedback getUserFeedback();

    public void checkRingBearerCorruption();

    public void playerWon(String currentPlayerId, String reason);

    public void playerLost(String currentPlayerId, String reason);

    public String getWinnerPlayerId();

    public LotroFormat getFormat();

    public boolean shouldAutoPass(String playerId, Phase phase);

    public boolean isSolo();
}
