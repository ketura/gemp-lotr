package com.gempukku.lotro.modifiers.condition.lotronly;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.Condition;

public class CanSpotTwilightCondition implements Condition {
    private final int _count;

    public CanSpotTwilightCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return game.getGameState().getTwilightPool() >= _count;
    }
}
