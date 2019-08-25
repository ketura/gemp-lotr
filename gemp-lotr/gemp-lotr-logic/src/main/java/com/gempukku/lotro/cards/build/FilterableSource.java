package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public interface FilterableSource {
    Filterable getFilterable(String playerId, LotroGame game, PhysicalCard source, EffectResult effectResult, Effect effect);
}
