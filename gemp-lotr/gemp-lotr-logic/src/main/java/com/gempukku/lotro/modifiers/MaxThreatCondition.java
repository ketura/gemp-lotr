package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.game.DefaultGame;

public class MaxThreatCondition implements Condition {
    private final int _count;

    public MaxThreatCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return game.getGameState().getThreats() <= _count;
    }
}
