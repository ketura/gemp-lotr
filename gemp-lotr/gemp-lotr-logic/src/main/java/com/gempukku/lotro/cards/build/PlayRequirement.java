package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public interface PlayRequirement {
    boolean accepts(String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect);
}
