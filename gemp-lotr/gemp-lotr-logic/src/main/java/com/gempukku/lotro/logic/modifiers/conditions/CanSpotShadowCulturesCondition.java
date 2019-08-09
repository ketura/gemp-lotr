package com.gempukku.lotro.logic.modifiers.conditions;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CanSpotShadowCulturesCondition implements Condition {
    private String _playerId;
    private int _count;

    public CanSpotShadowCulturesCondition(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        return GameUtils.getSpottableShadowCulturesCount(gameState, modifiersQuerying, _playerId)>=_count;
    }
}
