package com.gempukku.lotro.adventure;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.Action;

public interface ShadowAI {
    public Action getNextShadowAction(DefaultGame game);
}
