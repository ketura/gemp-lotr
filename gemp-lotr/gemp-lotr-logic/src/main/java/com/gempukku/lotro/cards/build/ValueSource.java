package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public interface ValueSource {
    Evaluator getEvaluator(ActionContext actionContext, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect);

    default int getMinimum(ActionContext actionContext, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        return getEvaluator(actionContext, playerId, game, self, effectResult, effect).evaluateExpression(game, self);
    }

    default int getMaximum(ActionContext actionContext, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        return getEvaluator(actionContext, playerId, game, self, effectResult, effect).evaluateExpression(game, self);
    }
}
