package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CountSpottableEvaluator implements Evaluator {
    private final Evaluator _over;
    private final Filterable[] _filters;
    private final Evaluator _limit;

    public CountSpottableEvaluator(Filterable... filters) {
        this(null, filters);
    }

    public CountSpottableEvaluator(Integer limit, Filterable... filters) {
        this(0, limit, filters);
    }

    public CountSpottableEvaluator(int over, Integer limit, Filterable... filters) {
        _over = new ConstantEvaluator(over);
        _filters = filters;
        if(limit == null)
            _limit = new ConstantEvaluator(Integer.MAX_VALUE);
        else
            _limit = new ConstantEvaluator(limit);
    }

    public CountSpottableEvaluator(Evaluator over, Evaluator limit, Filterable... filters) {
        _over = over;
        _filters = filters;
        _limit = limit;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard self) {
        final int active = Math.max(0, Filters.countSpottable(game, _filters) - _over.evaluateExpression(game, self));
        if (_limit == null)
            return active;
        return Math.min(_limit.evaluateExpression(game, self), active);
    }
}
