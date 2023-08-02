package com.gempukku.lotro.modifiers.condition.lotronly;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.lotronly.LotroGameUtils;
import com.gempukku.lotro.modifiers.condition.Condition;

public class CanSpotShadowCulturesCondition implements Condition {
    private final String _playerId;
    private final int _count;

    public CanSpotShadowCulturesCondition(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return LotroGameUtils.getSpottableShadowCulturesCount(game, _playerId)>=_count;
    }
}
