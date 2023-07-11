package com.gempukku.lotro.filters;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public interface Filter extends Filterable {
    public boolean accepts(DefaultGame game, PhysicalCard physicalCard);
}
