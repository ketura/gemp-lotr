package com.gempukku.lotro.modifiers.evaluator;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class DivideEvaluator implements Evaluator {
    private final Evaluator _source;
    private final int divider;

    public DivideEvaluator(int divider, Evaluator source) {
        this.divider = divider;
        _source = source;
    }

    @Override
    public int evaluateExpression(DefaultGame game, LotroPhysicalCard self) {
        return _source.evaluateExpression(game, self) / divider;
    }
}
