package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public interface PlayRequirement {
    boolean accepts(LotroGame game, PhysicalCard self);
}
