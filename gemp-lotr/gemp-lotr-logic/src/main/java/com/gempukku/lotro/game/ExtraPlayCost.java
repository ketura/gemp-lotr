package com.gempukku.lotro.game;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.modifiers.Condition;

public interface ExtraPlayCost {
    void appendExtraCosts(LotroGame game, CostToEffectAction action, PhysicalCard card);

    boolean canPayExtraCostsToPlay(LotroGame game, PhysicalCard card);

    Condition getCondition();
}
