package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.state.LotroGame;

public interface Cost {
    public EffectResult.Type getType();

    public String getText(LotroGame game);

    public CostResolution playCost(LotroGame game);
}
