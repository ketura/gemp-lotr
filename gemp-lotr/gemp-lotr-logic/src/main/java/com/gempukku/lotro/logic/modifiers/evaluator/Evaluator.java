package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public interface Evaluator extends ValueSource {
    int evaluateExpression(LotroGame game, PhysicalCard cardAffected);

    @Override
    default Evaluator getEvaluator(ActionContext actionContext) {
        return this;
    }
}
