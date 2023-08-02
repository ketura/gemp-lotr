package com.gempukku.lotro.game;

import com.gempukku.lotro.cards.CardBlueprintLibrary;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.gamestate.UserFeedback;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.actions.ActionsEnvironment;
import com.gempukku.lotro.modifiers.ModifiersEnvironment;
import com.gempukku.lotro.modifiers.ModifiersQuerying;

import java.util.Set;

public interface DefaultGame {
    GameState getGameState();
    CardBlueprintLibrary getLotroCardBlueprintLibrary();
    ModifiersEnvironment getModifiersEnvironment();
    ModifiersQuerying getModifiersQuerying();
    ActionsEnvironment getActionsEnvironment();
    UserFeedback getUserFeedback();
    void playerWon(String currentPlayerId, String reason);
    void playerLost(String currentPlayerId, String reason);
    String getWinnerPlayerId();
    LotroFormat getFormat();
    boolean shouldAutoPass(String playerId, Phase phase);
    boolean isSolo();
    boolean checkPlayRequirements(LotroPhysicalCard card);
    Set<String> getPlayers();
}
