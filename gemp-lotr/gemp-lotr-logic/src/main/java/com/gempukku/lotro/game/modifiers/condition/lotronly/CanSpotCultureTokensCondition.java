package com.gempukku.lotro.game.modifiers.condition.lotronly;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.Condition;

import java.util.Map;

public class CanSpotCultureTokensCondition implements Condition {
    private final int _count;
    private Token _token;

    public CanSpotCultureTokensCondition(int count) {
        _count = count;
    }

    public CanSpotCultureTokensCondition(int count, Token token) {
        _count = count;
        _token = token;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        int count = 0;
        for (PhysicalCard physicalCard : Filters.filterActive(game, Filters.hasAnyCultureTokens(1))) {
            for (Map.Entry<Token, Integer> tokenCountEntry : game.getGameState().getTokens(physicalCard).entrySet()) {
                if ((_token == null && tokenCountEntry.getKey().getCulture() != null)
                        || (tokenCountEntry.getKey() == _token)) {
                    count += tokenCountEntry.getValue();
                    if (count >= _count)
                        return true;
                }
            }
        }
        return false;
    }
}
