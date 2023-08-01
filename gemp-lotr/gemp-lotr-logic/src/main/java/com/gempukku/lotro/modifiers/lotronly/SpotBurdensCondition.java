package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.Condition;

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
