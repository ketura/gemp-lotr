package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class DivideEvaluator implements Evaluator {
    private Evaluator _source;
    private int divider;

    public DivideEvaluator(int divider, Evaluator source) {
        this.divider = divider;
        _source = source;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard self) {
        return _source.evaluateExpression(game, self) / divider;
    }
}
