package com.gempukku.lotro.cards.modifiers.conditions;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Map;

public class CanSpotCultureTokensCondition implements Condition {
    private int _count;
    private Token _token;

    public CanSpotCultureTokensCondition(int count) {
        _count = count;
    }

    public CanSpotCultureTokensCondition(int count, Token token) {
        _count = count;
        _token = token;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        int count = 0;
        for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, Filters.hasAnyCultureTokens(1))) {
            for (Map.Entry<Token, Integer> tokenCountEntry : gameState.getTokens(physicalCard).entrySet()) {
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
