package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.Condition;

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
