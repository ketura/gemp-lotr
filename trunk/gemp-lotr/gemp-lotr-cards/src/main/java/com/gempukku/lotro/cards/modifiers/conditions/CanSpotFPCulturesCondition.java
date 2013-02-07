package com.gempukku.lotro.cards.modifiers.conditions;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CanSpotFPCulturesCondition implements Condition {
    private String _playerId;
    private int _count;

    public CanSpotFPCulturesCondition(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return GameUtils.getSpottableFPCulturesCount(gameState, modifiersQuerying, _playerId)>=_count;
    }
}
