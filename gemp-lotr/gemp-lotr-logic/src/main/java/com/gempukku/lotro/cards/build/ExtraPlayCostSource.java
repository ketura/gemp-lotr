package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.ExtraPlayCost;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public interface ExtraPlayCostSource {
    ExtraPlayCost getExtraPlayCost(LotroGame game, PhysicalCard card);
}
