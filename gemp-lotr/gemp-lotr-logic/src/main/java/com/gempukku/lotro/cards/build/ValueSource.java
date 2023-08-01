package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

public interface ValueSource {
    Evaluator getEvaluator(DefaultActionContext<DefaultGame> actionContext);

    default int getMinimum(DefaultActionContext<DefaultGame> actionContext) {
        return getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
    }

    default int getMaximum(DefaultActionContext<DefaultGame> actionContext) {
        return getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
    }
}
