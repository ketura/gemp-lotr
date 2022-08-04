package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.Condition;

public class CanSpotShadowCulturesCondition implements Condition {
    private final String _playerId;
    private final int _count;

    public CanSpotShadowCulturesCondition(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        return GameUtils.getSpottableShadowCulturesCount(game, _playerId)>=_count;
    }
}
