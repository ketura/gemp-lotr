package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.game.state.LotroGame;

public interface Condition {
    boolean isFullfilled(LotroGame game);
}
