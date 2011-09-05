package com.gempukku.lotro.logic.actions;

import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

public interface CostToEffectAction extends Action {
    public void addCost(Effect cost);

    public void addEffect(Effect effect);
}
