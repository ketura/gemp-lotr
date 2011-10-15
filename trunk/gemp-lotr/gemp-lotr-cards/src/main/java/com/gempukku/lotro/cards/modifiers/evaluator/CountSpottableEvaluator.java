package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class CountSpottableEvaluator implements Evaluator {
    private Filterable[] _filters;
    private int _limit;

    public CountSpottableEvaluator(Filterable... filters) {
        this(Integer.MAX_VALUE, filters);
    }

    public CountSpottableEvaluator(int limit, Filterable... filters) {
        _filters = filters;
        _limit = limit;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return Math.min(_limit, Filters.countSpottable(gameState, modifiersQuerying, _filters));
    }
}
