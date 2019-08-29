package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

public interface Evaluator extends ValueSource {
    int evaluateExpression(LotroGame game, PhysicalCard cardAffected);

    @Override
    default Evaluator getEvaluator(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
        return this;
    }
}
