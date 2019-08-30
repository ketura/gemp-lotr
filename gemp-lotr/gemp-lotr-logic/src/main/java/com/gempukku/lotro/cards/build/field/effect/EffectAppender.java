package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public interface EffectAppender {
    void appendCost(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect);

    void appendEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect);

    boolean isPlayableInFull(ActionContext actionContext, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect);

    default boolean isPlayabilityCheckedForEffect() {
        return false;
    }
}
