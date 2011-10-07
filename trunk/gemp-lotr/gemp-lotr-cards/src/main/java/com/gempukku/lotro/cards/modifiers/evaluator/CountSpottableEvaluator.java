package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class CountSpottableEvaluator implements Evaluator {
    private Filter _filter;

    public CountSpottableEvaluator(Filter filter) {
        _filter = filter;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return Filters.countSpottable(gameState, modifiersQuerying, _filter);
    }
}
