package com.gempukku.lotro.game.modifiers.condition;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.Condition;

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
