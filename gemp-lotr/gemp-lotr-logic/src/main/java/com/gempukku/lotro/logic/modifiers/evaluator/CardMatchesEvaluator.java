package com.gempukku.lotro.logic.modifiers.evaluator;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CardMatchesEvaluator implements Evaluator {
    private final Filterable[] _filters;
    private final Evaluator _matches;
    private final int _default;

    public CardMatchesEvaluator(int defaultValue, Evaluator matches, Filterable... filters) {
        _default = defaultValue;
        _matches = matches;
        _filters = filters;
    }

    public CardMatchesEvaluator(int defaultValue, int matches, Filterable... filters) {
        _default = defaultValue;
        _matches = new ConstantEvaluator(matches);
        _filters = filters;
    }

    @Override
    public int evaluateExpression(LotroGame game, PhysicalCard self) {
        return Filters.and(_filters).accepts(game, self) ? _matches.evaluateExpression(game, self) : _default;
    }
}
