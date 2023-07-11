package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.game.DefaultGame;

public interface Condition {
    boolean isFullfilled(DefaultGame game);
}
