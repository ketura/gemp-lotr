package com.gempukku.lotro.modifiers.evaluator;

import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public interface Evaluator extends ValueSource {
    int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected);
    @Override
    default Evaluator getEvaluator(DefaultActionContext<DefaultGame> actionContext) {
        return this;
    }
}
