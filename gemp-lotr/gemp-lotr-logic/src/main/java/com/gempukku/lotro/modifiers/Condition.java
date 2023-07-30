package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.game.DefaultGame;

public interface Condition {
    boolean isFullfilled(DefaultGame game);
}
