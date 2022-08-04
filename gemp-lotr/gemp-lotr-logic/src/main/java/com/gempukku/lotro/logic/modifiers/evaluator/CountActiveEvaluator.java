package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CountActiveEvaluator implements Evaluator {
    private final int _over;
    private final Filterable[] _filters;
    private final Integer _limit;

    public CountActiveEvaluator(Filterable... filters) {
        this(null, filters);
    }

    public CountActiveEvaluator(Integer limit, Filterable... filters) {
        this(0, limit, filters);
    }

    public CountActiveEvaluator(int over, Integer limit, Filterable... filters) {
        _over = over;
        _filters = filters;
        _limit = limit;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard self) {
        final int active = Math.max(0, Filters.countActive(game, _filters) - _over);
        if (_limit == null)
            return active;
        return Math.min(_limit, active);
    }
}
