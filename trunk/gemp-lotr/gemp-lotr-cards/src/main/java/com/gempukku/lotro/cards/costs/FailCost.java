package com.gempukku.lotro.cards.costs;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Cost;
import com.gempukku.lotro.logic.timing.CostResolution;
import com.gempukku.lotro.logic.timing.EffectResult;

public class FailCost implements Cost {
    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public EffectResult.Type getType() {
        return null;
    }

    @Override
    public CostResolution playCost(LotroGame game) {
        return new CostResolution(null, false);
    }
}
