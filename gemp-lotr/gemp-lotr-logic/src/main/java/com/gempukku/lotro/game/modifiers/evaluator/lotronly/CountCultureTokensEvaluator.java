package com.gempukku.lotro.game.modifiers.evaluator.lotronly;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.evaluator.Evaluator;

import java.util.Map;

public class CountCultureTokensEvaluator implements Evaluator {
    private final Filterable[] _filters;
    private Token _token;

    public CountCultureTokensEvaluator(Filterable... filters) {
        _filters = filters;
    }

    public CountCultureTokensEvaluator(Token token, Filterable... filters) {
        _token = token;
        _filters = filters;
    }

    @Override
    public int evaluateExpression(DefaultGame game, PhysicalCard cardAffected) {
        int count = 0;
        for (PhysicalCard physicalCard : Filters.filterActive(game, Filters.and(_filters, Filters.hasAnyCultureTokens(1)))) {
            for (Map.Entry<Token, Integer> tokens : game.getGameState().getTokens(physicalCard).entrySet()) {
                if (tokens.getKey().getCulture() != null
                        && ((_token == null) || tokens.getKey() == _token))
                    count += tokens.getValue();
            }
        }

        return count;
    }
}
