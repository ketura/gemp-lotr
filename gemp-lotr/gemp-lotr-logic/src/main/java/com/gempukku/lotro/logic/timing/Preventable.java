package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public interface Preventable {
    public void prevent();

    public boolean isPrevented(LotroGame game);
}
