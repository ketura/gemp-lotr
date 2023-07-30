package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.game.DefaultGame;

public class SpotBurdensCondition implements Condition {
    private final int _count;

    public SpotBurdensCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return game.getGameState().getBurdens() >= _count;
    }
}
