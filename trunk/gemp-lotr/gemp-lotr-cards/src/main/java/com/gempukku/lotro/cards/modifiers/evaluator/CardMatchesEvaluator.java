package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

public class CardMatchesEvaluator implements Evaluator {
    private Filter[] _filters;
    private int _matches;
    private int _default;

    public CardMatchesEvaluator(int defaultValue, int matches, Filter... filters) {
        _default = defaultValue;
        _matches = matches;
        _filters = filters;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return Filters.and(_filters).accepts(gameState, modifiersQuerying, self) ? _matches : _default;
    }
}
