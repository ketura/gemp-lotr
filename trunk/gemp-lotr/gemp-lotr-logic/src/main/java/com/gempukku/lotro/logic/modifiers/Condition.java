package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.state.GameState;

public interface Condition {
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying);
}
