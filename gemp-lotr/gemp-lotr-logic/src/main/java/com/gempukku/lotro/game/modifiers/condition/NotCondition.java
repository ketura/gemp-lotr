package com.gempukku.lotro.game.modifiers.condition;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.Condition;

public class NotCondition implements Condition {
    private final Condition _condition;

    public NotCondition(Condition condition) {
        _condition = condition;
    }

    @Override
    public boolean isFullfilled(DefaultGame game) {
        return !_condition.isFullfilled(game);
    }
}
