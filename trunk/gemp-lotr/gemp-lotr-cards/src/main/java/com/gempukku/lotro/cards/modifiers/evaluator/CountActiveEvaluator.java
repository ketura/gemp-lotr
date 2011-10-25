package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class CountActiveEvaluator implements Evaluator {
    private int _over;
    private Filterable[] _filters;
    private Integer _limit;

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
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        final int active = Math.max(0, Filters.countSpottable(gameState, modifiersQuerying, _filters) - _over);
        if (_limit == null)
            return active;
        return Math.min(_limit, active);
    }
}
