package com.gempukku.lotro.game.modifiers.condition;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.modifiers.Condition;

public class CanSpotShadowCulturesCondition implements Condition {
    private final String _playerId;
    private final int _count;

    public CanSpotShadowCulturesCondition(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return GameUtils.getSpottableShadowCulturesCount(game, _playerId)>=_count;
    }
}
