package com.gempukku.lotro.game.modifiers.evaluator;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public interface Evaluator extends ValueSource {
    int evaluateExpression(DefaultGame game, LotroPhysicalCard cardAffected);

    @Override
    default Evaluator getEvaluator(ActionContext actionContext) {
        return this;
    }
}
