package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public interface TwilightCostModifierSource {
    int getTwilightCostModifier(LotroGame game, PhysicalCard card);
}
