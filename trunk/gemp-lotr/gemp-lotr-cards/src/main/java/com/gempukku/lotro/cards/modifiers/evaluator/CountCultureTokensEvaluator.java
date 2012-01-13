package com.gempukku.lotro.cards.modifiers.evaluator;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Map;

public class CountCultureTokensEvaluator implements Evaluator {
    private Filterable[] _filters;

    public CountCultureTokensEvaluator(Filterable... filters) {
        _filters = filters;
    }

    @Override
    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
        int count = 0;
        for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, Filters.and(_filters, Filters.hasAnyCultureTokens(1)))) {
            for (Map.Entry<Token, Integer> tokens : gameState.getTokens(physicalCard).entrySet()) {
                if (tokens.getKey().getCulture() != null)
                    count += tokens.getValue();
            }
        }

        return count;
    }
}
