package com.gempukku.lotro.modifiers.condition.lotronly;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.lotronly.LotroGameUtils;
import com.gempukku.lotro.modifiers.condition.Condition;

public class CanSpotFPCulturesCondition implements Condition {
    private final String _playerId;
    private final int _count;

    public CanSpotFPCulturesCondition(String playerId, int count) {
        _playerId = playerId;
        _count = count;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return LotroGameUtils.getSpottableFPCulturesCount(game, _playerId)>=_count;
    }
}
