package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public interface TriggerChecker {
    boolean isTriggerValid(String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect);

    boolean isBefore();
}
