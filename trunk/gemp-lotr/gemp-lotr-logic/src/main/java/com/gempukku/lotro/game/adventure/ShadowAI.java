package com.gempukku.lotro.game.adventure;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

public interface ShadowAI {
    public Action getNextShadowAction(LotroGame game);
}
