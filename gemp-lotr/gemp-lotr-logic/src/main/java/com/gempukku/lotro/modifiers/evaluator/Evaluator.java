package com.gempukku.lotro.modifiers.evaluator;

import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.ValueSource;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public interface Evaluator<AbstractGame extends DefaultGame> extends ValueSource<AbstractGame> {
    int evaluateExpression(AbstractGame game, LotroPhysicalCard cardAffected);
    @Override
    default Evaluator getEvaluator(DefaultActionContext<AbstractGame> actionContext) {
        return this;
    }
}
