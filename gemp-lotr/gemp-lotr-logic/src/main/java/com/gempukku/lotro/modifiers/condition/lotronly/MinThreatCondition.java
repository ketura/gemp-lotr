package com.gempukku.lotro.modifiers.condition.lotronly;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.Condition;

public class MinThreatCondition implements Condition {
    private final int _count;

    public MinThreatCondition(int count) {
        _count = count;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return game.getGameState().getThreats() >= _count;
    }
}
