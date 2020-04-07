package com.gempukku.lotro.filters;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public interface Filter extends Filterable {
    public boolean accepts(LotroGame game, PhysicalCard physicalCard);
}
