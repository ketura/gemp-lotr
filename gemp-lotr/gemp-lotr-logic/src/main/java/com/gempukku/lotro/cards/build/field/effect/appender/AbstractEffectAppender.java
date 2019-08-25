package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public abstract class AbstractEffectAppender implements EffectAppender {
    @Override
    public final void appendCost(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        action.appendCost(createEffect(action, playerId, game, self, effectResult, effect));
    }

    @Override
    public final void appendEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        action.appendEffect(createEffect(action, playerId, game, self, effectResult, effect));
    }

    protected abstract Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect);
}
