package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.evaluator.Evaluator;

public interface ValueSource<AbstractGame extends DefaultGame> {
    Evaluator getEvaluator(DefaultActionContext<AbstractGame> actionContext);

    default int getMinimum(DefaultActionContext<AbstractGame> actionContext) {
        return getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
    }

    default int getMaximum(DefaultActionContext<AbstractGame> actionContext) {
        return getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), null);
    }
}
