package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public interface ExtraPossessionClassTest {
    boolean isExtraPossessionClass(LotroGame game, PhysicalCard self, PhysicalCard attachedTo);
}
