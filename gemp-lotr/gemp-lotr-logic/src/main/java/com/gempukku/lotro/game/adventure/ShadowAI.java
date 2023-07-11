package com.gempukku.lotro.game.adventure;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.Action;

public interface ShadowAI {
    public Action getNextShadowAction(DefaultGame game);
}
