package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public interface ChooseableCost extends Cost {
    public boolean canPlayCost(LotroGame game);
}
